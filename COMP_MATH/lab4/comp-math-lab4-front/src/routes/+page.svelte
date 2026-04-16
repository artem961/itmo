<script lang="ts">
	import { onMount } from 'svelte';
	import functionPlot from 'function-plot';
	import { slide, fade } from 'svelte/transition';

	// --- Интерфейсы ---
	interface Point { x: number; y: number; }
	interface ApproxResult {
		type: { id: number; label: string };
		func: string;
		s: number;
		determ: number;
		delta: number;
		pearson: number | null;
		coffs: number[];
	}

	const API_BASE = 'https://comp-math.arhr.tech/lab4/api/approx';
	
	// Палитра: Степенная (5) - Почти черная
	const COLORS = ['#6366f1', '#d946ef', '#f59e0b', '#ef4444', '#22c55e', '#111827'];

	// --- Состояние ---
	let points = $state<Point[]>([
		{x: 1.1, y: 2.73}, {x: 2.3, y: 5.12}, {x: 3.7, y: 7.74}, {x: 4.5, y: 8.91},
		{x: 5.4, y: 10.59}, {x: 6.8, y: 12.75}, {x: 7.5, y: 14.1}, {x: 8.2, y: 15.5}
	]);
	
	let activePointIndex = $state(0);
	let approxResults = $state<ApproxResult[]>([]);
	let bestType = $state<{id: number; label: string} | null>(null);
	let expandedId = $state<number | null>(null);
	let visibleMethods = $state<Set<number>>(new Set());

	let isLoading = $state(false);
	let errorMsg = $state<string | null>(null);
	let plotContainer: HTMLDivElement | undefined = $state();
	let instance: any = null;

	let shouldResetScale = true;

	/**
	 * Очистка формулы для function-plot и buildSafePoints.
	 * Исправляет проблему отсутствия знака умножения (например, 6.37log -> 6.37*log)
	 */
	function cleanFormula(f: string): string {
		return f
			.replace(/y\s*=\s*/g, '')
			.replace(/·/g, '*')
			.replace(/x²/g, 'x^2')
			.replace(/x³/g, 'x^3')
			.replace(/ln\(/g, 'log(')
			.replace(/e\^/g, 'exp')
			// Вставляем умножение между числом и буквой: 6.37ln -> 6.37*log, 2x -> 2*x
			.replace(/(\d)([a-z])/g, '$1*$2') 
			.replace(/(\))(\d)/g, '$1*$2')
			.replace(/(\))([a-z])/g, '$1*$2');
	}

	/**
	 * Ручная генерация точек для функций, чувствительных к x <= 0 (Log, Power)
	 */
	function buildSafePoints(fStr: string): [number, number][] {
		const jsStr = fStr
			.replace(/log\(/g, 'Math.log(')
			.replace(/exp\(/g, 'Math.exp(')
			.replace(/\^/g, '**');

		let evalFn: (x: number) => number;
		try {
			evalFn = new Function('x', `return ${jsStr}`) as (x: number) => number;
		} catch (e) {
			console.error("Ошибка парсинга функции для JS:", jsStr);
			return [];
		}

		const pts: [number, number][] = [];
		// Генерируем точки от 0.01 до 15 с мелким шагом
		for (let xi = 0.01; xi <= 15; xi += 0.05) {
			try {
				const yi = evalFn(xi);
				if (isFinite(yi) && !isNaN(yi)) pts.push([xi, yi]);
			} catch { }
		}
		return pts;
	}

	$effect(() => {
		if (plotContainer && (approxResults || visibleMethods)) {
			drawPlot();
		}
	});

	function drawPlot() {
		if (!plotContainer) return;

		let xDomain = [-2, 12];
		let yDomain = [-2, 25];
		
		if (!shouldResetScale && instance) {
			xDomain = instance.meta.xScale.domain();
			yDomain = instance.meta.yScale.domain();
		}
		shouldResetScale = false;

		const data: any[] = [{
			points: points.map(p => [p.x, p.y]),
			fnType: 'points', graphType: 'scatter', color: '#64748b', attr: { r: 4 }
		}];

		if (points[activePointIndex]) {
			data.push({
				points: [[points[activePointIndex].x, points[activePointIndex].y]],
				fnType: 'points', graphType: 'scatter', 
				color: '#ef4444', 
				attr: { r: 4, 'stroke-width': 4, fill: '#64748b' } 
			});
		}

		approxResults.forEach((res) => {
			if (!visibleMethods.has(res.type.id)) return;

			const fStr = cleanFormula(res.func);
			const color = COLORS[res.type.id % COLORS.length];

			if (res.type.id === 4 || res.type.id === 5) {
				const pts = buildSafePoints(fStr);
				if (pts.length > 0) {
					data.push({ 
						points: pts, 
						fnType: 'points', 
						graphType: 'polyline', 
						color 
					});
				}
			} else {
				data.push({ 
					fn: fStr, 
					color, 
					graphType: 'polyline', // Явно указываем тип линии
					attr: { fill: 'none' },  // Принудительно убираем заливку
					nSamples: 1000 
				});
			}
		});

		instance = functionPlot({
			target: plotContainer,
			width: plotContainer.clientWidth,
			height: 520,
			grid: true,
			data: data,
			xAxis: { domain: xDomain },
			yAxis: { domain: yDomain }
		});

		const svg = plotContainer.querySelector('svg');
		if (svg) {
			svg.onclick = (e) => {
				const rect = svg.getBoundingClientRect();
				const xPixel = e.clientX - rect.left;
				const yPixel = e.clientY - rect.top;
				const x = instance.meta.xScale.invert(xPixel - instance.meta.margin.left);
				const y = instance.meta.yScale.invert(yPixel - instance.meta.margin.top);

				points[activePointIndex].x = Number(x.toFixed(2));
				points[activePointIndex].y = Number(y.toFixed(2));
				
				shouldResetScale = true; 
				activePointIndex = (activePointIndex + 1) % points.length;
			};
		}
	}

	async function handleSubmit() {
		if (points.length < 8 || points.length > 12) return;
		isLoading = true; errorMsg = null;
		try {
			const res = await fetch(API_BASE, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ points: points }) 
			});
			const data = await res.json();
			if (data.error) errorMsg = data.error;
			else {
				approxResults = data.results;
				bestType = data.best;
				expandedId = data.best.id;
				visibleMethods = new Set(data.results.map(r => r.type.id));
				shouldResetScale = true;
			}
		} catch { errorMsg = "Ошибка сети"; }
		finally { isLoading = false; }
	}

	const addPoint = () => points.length < 12 && (points = [...points, {x: 0, y: 0}]);
	const removePoint = (i: number) => {
		if (points.length > 8) {
			points = points.filter((_, idx) => idx !== i);
			if (activePointIndex >= points.length) activePointIndex = 0;
		}
	};
</script>

<div class="app-container">
	<header class="app-header">
		<div class="header-left">
			<div class="logo-icon">Σ</div>
			<div class="logo-text">
				<h1>CompMath</h1>
				<span>Лабораторная работа №4</span>
			</div>
		</div>
		<div class="header-right">
			<div class="author-info">
				<span class="name">Храбров Артём</span>
			</div>
		</div>
	</header>

	<div class="main-layout">
		<!-- ВВОД -->
		<aside class="panel left-panel">
			<div class="panel-head">
				<h2>Точки данных</h2>
				<button class="btn-add" onclick={addPoint} disabled={points.length >= 12}>+ Добавить</button>
			</div>
			
			<div class="points-scroll">
				{#each points as pt, i}
					<div class="point-row" class:active={activePointIndex === i} onclick={() => activePointIndex = i}>
						<span class="point-idx">X{i+1}</span>
						<div class="point-inputs">
							<input type="number" step="0.1" bind:value={pt.x} placeholder="x" />
							<input type="number" step="0.1" bind:value={pt.y} placeholder="y" />
						</div>
						<button class="btn-remove" onclick={(e) => { e.stopPropagation(); removePoint(i); }} disabled={points.length <= 8}>×</button>
					</div>
				{/each}
			</div>

			<button class="btn-run" onclick={handleSubmit} disabled={isLoading || points.length < 8}>
				{#if isLoading} <div class="loader"></div> {/if}
				Рассчитать
			</button>
		</aside>

		<!-- ГРАФИК -->
		<main class="center-content">
			<div class="chart-wrapper">
				<div class="chart-legend">
					{#each approxResults as res}
						{#if visibleMethods.has(res.type.id)}
							<div class="leg-item" style="color: {COLORS[res.type.id % COLORS.length]}" in:fade>
								<i style="background: {COLORS[res.type.id % COLORS.length]}"></i>
								{res.type.label}
							</div>
						{/if}
					{/each}
				</div>
				<div class="plot-area" bind:this={plotContainer}></div>
			</div>
			
			{#if errorMsg}
				<div class="error-msg" in:slide>{errorMsg}</div>
			{/if}
		</main>

		<!-- РЕЗУЛЬТАТЫ -->
		<aside class="panel right-panel">
			<div class="panel-head">
				<h2>Результаты аппроксимаций</h2>
			</div>

			<div class="results-container">
				{#if approxResults.length === 0}
					<div class="placeholder-text">Введите точки и рассчитайте</div>
				{:else}
					{#each approxResults as res}
						<div class="approx-card" 
							 class:expanded={expandedId === res.type.id}
							 class:best={bestType?.id === res.type.id}
							 class:muted={!visibleMethods.has(res.type.id)}
							 onclick={() => expandedId = expandedId === res.type.id ? null : res.type.id}>
							
							<div class="card-head">
								<div class="card-title">
									<i class="color-dot" style="background: {COLORS[res.type.id % COLORS.length]}"></i>
									<strong>{res.type.label}</strong>
									{#if bestType?.id === res.type.id}<span class="best-badge">САМАЯ ТОЧНАЯ</span>{/if}
									<span class="delta-preview">δ: {res.delta.toFixed(3)}</span>
								</div>
								<input type="checkbox" checked={visibleMethods.has(res.type.id)} 
									   onclick={(e) => { e.stopPropagation(); 
										   if (visibleMethods.has(res.type.id)) visibleMethods.delete(res.type.id);
										   else visibleMethods.add(res.type.id);
										   visibleMethods = new Set(visibleMethods);
									   }} />
							</div>

							<div class="card-formula">{res.func}</div>

							{#if expandedId === res.type.id}
								<div class="card-details" transition:slide>
									<div class="grid-metrics">
										<div class="item"><span>S</span> <b>{res.s.toFixed(6)}</b></div>
										<div class="item"><span>δ</span> <b>{res.delta.toFixed(4)}</b></div>
										<div class="item"><span>R²</span> <b>{res.determ.toFixed(4)}</b></div>
										{#if res.type.id === 0 && res.pearson !== null}
											<div class="item highlight"><span>Пирсон</span> <b>{res.pearson.toFixed(4)}</b></div>
										{/if}
									</div>
									<div class="coffs-box">
										<label>Коэффициенты:</label>
										<div class="coffs-grid">
											{#each res.coffs.slice().reverse() as c, idx}
												<span>a{res.coffs.length - 1 - idx} = <b>{c.toFixed(4)}</b></span>
											{/each}
										</div>
									</div>
								</div>
							{/if}
						</div>
					{/each}
				{/if}
			</div>
		</aside>
	</div>
</div>

<style>
	:global(body) { margin: 0; font-family: 'Inter', system-ui, sans-serif; background: #f8fafc; color: #1e293b; overflow: hidden; }
	.app-container { display: flex; flex-direction: column; height: 100vh; }

	/* HEADER */
	.app-header { background: #fff; border-bottom: 1px solid #e2e8f0; padding: 0 2rem; display: flex; justify-content: space-between; align-items: center; height: 64px; }
	.header-left { display: flex; align-items: center; gap: 15px; }
	.logo-icon { background: #6366f1; color: #fff; width: 34px; height: 34px; display: flex; align-items: center; justify-content: center; border-radius: 9px; font-weight: 900; font-size: 1.1rem; }
	.logo-text h1 { margin: 0; font-size: 1rem; line-height: 1; }
	.logo-text span { font-size: 0.7rem; color: #94a3b8; font-weight: 500; }
	.author-info { text-align: right; line-height: 1.1; }
	.author-info .label { font-size: 0.55rem; color: #94a3b8; text-transform: uppercase; font-weight: 700; display: block; }
	.author-info .name { font-size: 0.75rem; font-weight: 600; color: #64748b; }

	/* LAYOUT */
	.main-layout { display: grid; grid-template-columns: 260px 1fr 360px; flex: 1; overflow: hidden; }
	.panel { background: #fff; display: flex; flex-direction: column; border-right: 1px solid #e2e8f0; padding: 1.25rem; }
	.right-panel { border-right: none; border-left: 1px solid #e2e8f0; }
	.panel-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
	.panel-head h2 { font-size: 0.65rem; font-weight: 800; color: #94a3b8; text-transform: uppercase; letter-spacing: 1px; margin: 0; }

	/* POINTS */
	.points-scroll { flex: 1; overflow-y: auto; display: flex; flex-direction: column; gap: 6px; padding-right: 4px; }
	.point-row { display: flex; align-items: center; gap: 8px; padding: 6px 10px; border: 1px solid #f1f5f9; border-radius: 8px; cursor: pointer; transition: 0.15s; }
	.point-row.active { border-color: #ef4444; background: #fff1f2; }
	.point-idx { font-size: 0.7rem; font-weight: 900; color: #94a3b8; width: 22px; }
	.point-inputs { display: flex; gap: 4px; flex: 1; }
	.point-inputs input { width: 100%; padding: 5px; border: 1px solid #e2e8f0; border-radius: 5px; font-size: 0.8rem; outline: none; }
	.btn-remove { border: none; background: none; color: #cbd5e1; font-size: 1.1rem; cursor: pointer; }
	.btn-add { background: #f1f5f9; border: none; padding: 5px 12px; border-radius: 6px; font-size: 0.65rem; font-weight: 800; color: #475569; cursor: pointer; }
	.btn-run { width: 100%; background: #6366f1; color: #fff; border: none; padding: 14px; border-radius: 12px; font-weight: 700; margin-top: 1rem; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 8px; }
	.btn-run:disabled { background: #cbd5e1; cursor: not-allowed; }

	/* CHART */
	.center-content { background: #f1f5f9; padding: 1rem; display: flex; flex-direction: column; align-items: center; justify-content: center; }
	.chart-wrapper { background: #fff; padding: 1.5rem; border-radius: 20px; box-shadow: 0 4px 20px -5px rgba(0,0,0,0.05); width: 100%; max-width: 900px; }
	.chart-legend { display: flex; flex-wrap: wrap; gap: 15px; justify-content: center; margin-bottom: 1rem; }
	.leg-item { display: flex; align-items: center; gap: 6px; font-size: 0.75rem; font-weight: 800; }
	.leg-item i { width: 8px; height: 8px; border-radius: 50%; }
	.plot-area { width: 100%; height: 520px; }
	.click-hint { font-size: 0.7rem; color: #94a3b8; text-align: center; margin-top: 1rem; font-style: italic; }

	/* CARDS */
	.results-container { flex: 1; overflow-y: auto; display: flex; flex-direction: column; gap: 10px; }
	.approx-card { border: 1px solid #e2e8f0; border-radius: 14px; padding: 12px; cursor: pointer; transition: 0.2s; background: #fff; }
	.approx-card:hover { border-color: #6366f1; }
	.approx-card.best { border-color: #22c55e; background: #f0fdf4; }
	.approx-card.muted { opacity: 0.4; filter: grayscale(1); }
	.approx-card.expanded { box-shadow: 0 10px 15px -3px rgba(0,0,0,0.05); border-color: #6366f1; }

	.card-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
	.card-title { display: flex; align-items: center; gap: 8px; }
	.color-dot { width: 10px; height: 10px; border-radius: 50%; }
	.card-title strong { font-size: 0.8rem; }
	.delta-preview { font-size: 0.7rem; color: #94a3b8; margin-left: 8px; font-weight: 500; }
	.best-badge { font-size: 0.55rem; background: #22c55e; color: #fff; padding: 2px 6px; border-radius: 4px; font-weight: 800; }

	.card-formula { font-family: 'JetBrains Mono', monospace; font-size: 0.75rem; color: #6366f1; font-weight: 500; }
	.card-details { margin-top: 12px; padding-top: 12px; border-top: 1px dashed #e2e8f0; }
	
	.grid-metrics { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; margin-bottom: 12px; }
	.grid-metrics .item { font-size: 0.75rem; color: #64748b; }
	.grid-metrics .item span { color: #6366f1; font-weight: 800; margin-right: 4px; }
	.grid-metrics .item b { color: #1e293b; font-weight: 800; }
	.grid-metrics .highlight { grid-column: span 2; }

	.coffs-box label { font-size: 0.6rem; font-weight: 800; color: #94a3b8; text-transform: uppercase; display: block; margin-bottom: 5px; }
	.coffs-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 4px; background: #f8fafc; padding: 8px; border-radius: 8px; }
	.coffs-grid span { font-size: 0.7rem; font-family: monospace; color: #64748b; }
	.coffs-grid b { color: #1e293b; }

	.error-msg { background: #ef4444; color: #fff; padding: 10px 20px; border-radius: 10px; margin-top: 1rem; font-size: 0.8rem; font-weight: 600; }
	.loader { width: 14px; height: 14px; border: 2px solid rgba(255,255,255,0.3); border-top-color: #fff; border-radius: 50%; animation: spin 0.8s linear infinite; }
	@keyframes spin { to { transform: rotate(360deg); } }
	.placeholder-text { text-align: center; color: #94a3b8; padding: 3rem 1rem; font-size: 0.8rem; font-style: italic; }
</style>