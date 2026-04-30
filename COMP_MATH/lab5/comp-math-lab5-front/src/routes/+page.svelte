<script lang="ts">
	import { onMount } from 'svelte';
	import functionPlot from 'function-plot';
	import { slide, fade } from 'svelte/transition';

	interface Point { x: number; y: number; }
	interface InterpolationResult {
		type: { id: number; label: string };
		x: number;
		y: number | null;
		message: string | null;
		graphicPoints: Point[] | null;
	}

	interface ModeData {
		points: Point[];
		results: InterpolationResult[];
		diffTable: number[][];
		visibleMethods: Set<number>;
	}

	const API_BASE = 'https://comp-math.arhr.tech/lab5/api/interpolate';
	const COLORS = ['#2563eb', '#dc2626', '#d97706', '#16a34a', '#9333ea', '#0891b2'];

	let inputMode = $state<'table' | 'file' | 'function'>('table');
	let targetX = $state<number>(1.5);

	let tableModeData = $state<ModeData>({
		points: [{x: 1, y: 1}, {x: 2, y: 2}, {x: 3, y: 3}, {x: 4, y: 4}],
		results: [], diffTable: [], visibleMethods: new Set()
	});
	let fileModeData = $state<ModeData>({
		points: [], results: [], diffTable: [], visibleMethods: new Set()
	});
	let funcModeData = $state<ModeData>({
		points: [], results: [], diffTable: [], visibleMethods: new Set()
	});

	let currentModeData = $derived(
		inputMode === 'table' ? tableModeData :
		inputMode === 'file' ? fileModeData :
		funcModeData
	);

	let selectedFunc = $state<'sin' | 'poly' | 'ln'>('sin');
	let funcA = $state<number>(0.1);
	let funcB = $state<number>(10);
	let funcH = $state<number>(1);
	let funcError = $state<string | null>(null);

	let isLoading = $state(false);
	let errorMsg = $state<string | null>(null);
	let isTableModalOpen = $state(false);

	let plotContainer: HTMLDivElement | undefined = $state();
	let instance: any = null;
	let shouldResetScale = true;
	let activePointIndex = $state(0);
	let fileInput: HTMLInputElement | undefined = $state();

	$effect(() => {
		if (inputMode === 'function') {
			funcError = null;
			if (funcH <= 0 || funcA >= funcB) {
				funcError = funcH <= 0 ? "Шаг h должен быть > 0" : "a должно быть < b";
				funcModeData.points = [];
				return;
			}

			let n = Math.floor((funcB - funcA) / funcH) + 1;
			let lastX = funcA + (n - 1) * funcH;

			if (lastX > funcB + 1e-8) {
				n--;
				lastX = funcA + (n - 1) * funcH;
			}

			if (n < 2) {
				funcError = "Шаг h слишком большой. Нужно мин. 2 точки.";
				funcModeData.points = [];
				return;
			}
			if (n > 20) {
				funcError = `При h=${funcH} получается ${n} точек. Max 20.`;
				funcModeData.points = [];
				return;
			}

			let newPoints: Point[] = [];
			for (let i = 0; i < n; i++) {
				let x = funcA + i * funcH;
				let y = 0;
				if (selectedFunc === 'sin') y = Math.sin(x);
				else if (selectedFunc === 'poly') y = Math.pow(x, 3) - 0.25 * Math.pow(x, 2) + x;
				else if (selectedFunc === 'ln') y = x > 0 ? Math.log(x) : 0;
				newPoints.push({ x: Number(x.toFixed(4)), y: Number(y.toFixed(4)) });
			}
			funcModeData.points = newPoints;
			shouldResetScale = true;
		}
	});

	$effect(() => {
		if (plotContainer && inputMode) {
			drawPlot();
		}
	});

	function drawPlot() {
		if (!plotContainer) return;
		const { points: currentPts, results: currentResults, visibleMethods: currentVisibleMethods } = currentModeData;

		let xDomain = [-2, 12];
		let yDomain = [-2, 12];
		
		if (!shouldResetScale && instance) {
			xDomain = instance.meta.xScale.domain();
			yDomain = instance.meta.yScale.domain();
		}
		shouldResetScale = false;

		const data: any[] = [];

		currentResults.forEach((res) => {
			if (currentVisibleMethods.has(res.type.id) && Array.isArray(res.graphicPoints) && res.graphicPoints.length > 0) {
				const linePts = res.graphicPoints
					.filter(p => p.x !== null && p.y !== null && !isNaN(p.x) && !isNaN(p.y))
					.map(p => [Number(p.x), Number(p.y)]);

				if (linePts.length > 0) {
					data.push({
						points: linePts,
						fnType: 'points', 
						graphType: 'polyline', 
						color: COLORS[res.type.id % COLORS.length],
						attr: { fill: 'transparent', 'stroke-width': 2 }
					});
				}
			}
		});

		data.push({
			points: currentPts.map(p => [p.x, p.y]),
			fnType: 'points', graphType: 'scatter', color: '#1e293b', attr: { r: 4, fill: '#1e293b' }
		});

		if (inputMode === 'table' && currentPts[activePointIndex]) {
			data.push({
				points: [[currentPts[activePointIndex].x, currentPts[activePointIndex].y]],
				fnType: 'points', graphType: 'scatter', 
				color: '#ef4444', attr: { r: 5, 'stroke-width': 3, fill: '#f8fafc' } 
			});
		}

		plotContainer.innerHTML = '';

		instance = functionPlot({
			target: plotContainer,
			width: plotContainer.clientWidth,
			height: 520,
			grid: true,
			data: data,
			annotations: [{ x: targetX, text: 'X' }],
			xAxis: { domain: xDomain },
			yAxis: { domain: yDomain }
		});

		const svg = plotContainer.querySelector('svg');
		if (svg) {
			svg.onclick = (e) => {
				if (inputMode !== 'table') return;
				const rect = svg.getBoundingClientRect();
				const xPixel = e.clientX - rect.left;
				const yPixel = e.clientY - rect.top;
				const x = instance.meta.xScale.invert(xPixel - instance.meta.margin.left);
				const y = instance.meta.yScale.invert(yPixel - instance.meta.margin.top);

				tableModeData.points[activePointIndex].x = Number(x.toFixed(2));
				tableModeData.points[activePointIndex].y = Number(y.toFixed(2));
				shouldResetScale = true; 
				activePointIndex = (activePointIndex + 1) % tableModeData.points.length;
			};
		}
	}

	async function handleSubmit() {
		const currentPts = currentModeData.points;
		if (currentPts.length < 2 || currentPts.length > 20) {
			errorMsg = "Количество точек должно быть от 2 до 20";
			return;
		}
		isLoading = true; errorMsg = null;
		
		try {
			const res = await fetch(API_BASE, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ points: currentPts, x: targetX }) 
			});
			const data = await res.json();
			if (data.error) errorMsg = data.error;
			else {
				if (inputMode === 'table') {
					tableModeData.results = data.results;
					tableModeData.diffTable = data.diffTable || [];
					tableModeData.visibleMethods = new Set(data.results.map((r: any) => r.type.id));
				} else if (inputMode === 'file') {
					fileModeData.results = data.results;
					fileModeData.diffTable = data.diffTable || [];
					fileModeData.visibleMethods = new Set(data.results.map((r: any) => r.type.id));
				} else if (inputMode === 'function') {
					funcModeData.results = data.results;
					funcModeData.diffTable = data.diffTable || [];
					funcModeData.visibleMethods = new Set(data.results.map((r: any) => r.type.id));
				}
				shouldResetScale = true;
			}
		} catch { errorMsg = "Ошибка соединения с сервером"; }
		finally { isLoading = false; }
	}

	const addPoint = () => {
		if (tableModeData.points.length < 20) tableModeData.points = [...tableModeData.points, {x: 0, y: 0}];
	};
	
	const removePoint = (i: number) => {
		if (tableModeData.points.length > 2) {
			tableModeData.points = tableModeData.points.filter((_, idx) => idx !== i);
			if (activePointIndex >= tableModeData.points.length) activePointIndex = 0;
		}
	};

	function handleFileUpload(event: Event) {
		const target = event.target as HTMLInputElement;
		const file = target.files?.[0];
		if (!file) return;

		const reader = new FileReader();
		reader.onload = (e) => {
			const text = e.target?.result as string;
			const lines = text.split(/\r?\n/).filter(l => l.trim() !== "");
			const newPoints: Point[] = [];
			let hasError = false;

			for (let line of lines) {
				const parts = line.split(/[;,]/);
				if (parts.length < 2) continue;
				const x = parseFloat(parts[0].replace(",", "."));
				const y = parseFloat(parts[1].replace(",", "."));
				if (isNaN(x) || isNaN(y)) {
					errorMsg = "Ошибка в данных файла.";
					hasError = true;
					break;
				}
				newPoints.push({ x, y });
			}

			if (!hasError) {
				if (newPoints.length < 2 || newPoints.length > 20) {
					errorMsg = `Точек в файле: ${newPoints.length}. Требуется от 2 до 20.`;
				} else {
					fileModeData.points = newPoints;
					errorMsg = null;
					shouldResetScale = true;
					inputMode = 'file';
				}
			}
			target.value = "";
		};
		reader.readAsText(file);
	}
</script>

<div class="app">
	<!-- ШАПКА -->
	<header class="header">
		<div class="header-left">
			<div class="logo">I(x)</div>
			<div class="header-title">
				<span class="title-main">CompMath</span>
				<span class="title-sub">Лабораторная работа №5</span>
			</div>
		</div>
		<span class="author">Храбров Артём</span>
	</header>

	<div class="layout">
		<!-- ЛЕВАЯ ПАНЕЛЬ -->
		<aside class="sidebar">

			<!-- X -->
			<div class="x-box">
				<span class="x-label">X =</span>
				<input type="number" step="0.1" bind:value={targetX} class="x-input" />
			</div>

			<!-- Табы -->
			<div class="tabs">
				<button class:active={inputMode === 'table'} onclick={() => inputMode = 'table'}>Таблица</button>
				<button class:active={inputMode === 'file'} onclick={() => inputMode = 'file'}>Файл</button>
				<button class:active={inputMode === 'function'} onclick={() => inputMode = 'function'}>Функция</button>
			</div>

			<input type="file" accept=".csv" style="display:none" bind:this={fileInput} onchange={handleFileUpload} />

			<!-- Режим: Файл -->
			{#if inputMode === 'file'}
				<div class="file-zone" in:slide={{ duration: 180 }}>
					<button class="btn-file" onclick={() => fileInput?.click()}>
						<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>
						Загрузить CSV
					</button>
					{#if currentModeData.points.length === 0}
						<p class="file-hint">Файл не загружен</p>
					{/if}
				</div>
			{/if}

			<!-- Режим: Функция -->
			{#if inputMode === 'function'}
				<div class="func-panel" in:slide={{ duration: 180 }}>
					<label class="field-label">Функция</label>
					<select bind:value={selectedFunc} class="func-select">
						<option value="sin">y = sin(x)</option>
						<option value="poly">y = x³ − 0.25x² + x</option>
						<option value="ln">y = ln(x)</option>
					</select>
					<div class="func-params">
						<div class="param-group">
							<label>a</label>
							<input type="number" step="0.1" bind:value={funcA} />
						</div>
						<div class="param-group">
							<label>b</label>
							<input type="number" step="0.1" bind:value={funcB} />
						</div>
						<div class="param-group">
							<label>h</label>
							<input type="number" step="0.1" bind:value={funcH} />
						</div>
					</div>
					{#if funcError}
						<p class="func-error">{funcError}</p>
					{/if}
				</div>
			{/if}

			<!-- Список точек -->
			<div class="points-header">
				<span class="field-label">Узлы ({currentModeData.points.length}/20)</span>
				{#if inputMode === 'table'}
					<button class="btn-add" onclick={addPoint} disabled={tableModeData.points.length >= 20}>+ Добавить</button>
				{/if}
			</div>

			<div class="points-list" class:readonly={inputMode !== 'table'}>
				{#if inputMode === 'table'}
					{#each currentModeData.points as pt, i}
						<div class="point-row" class:is-active={activePointIndex === i} onclick={() => activePointIndex = i}>
							<span class="point-idx">{i}</span>
							<div class="point-fields">
								<input type="number" step="0.1" bind:value={tableModeData.points[i].x} onclick={(e) => e.stopPropagation()} />
								<input type="number" step="0.1" bind:value={tableModeData.points[i].y} onclick={(e) => e.stopPropagation()} />
							</div>
							<button class="btn-remove" onclick={(e) => { e.stopPropagation(); removePoint(i); }} disabled={tableModeData.points.length <= 2}>×</button>
						</div>
					{/each}
				{:else}
					{#each currentModeData.points as pt, i}
						<div class="point-row readonly">
							<span class="point-idx">{i}</span>
							<div class="point-fields">
								<input type="number" value={pt.x} disabled />
								<input type="number" value={pt.y} disabled />
							</div>
						</div>
					{/each}
				{/if}
			</div>

			<button
				class="btn-run"
				onclick={handleSubmit}
				disabled={isLoading || currentModeData.points.length < 2 || (inputMode === 'function' && funcError !== null)}
			>
				{#if isLoading}<span class="spinner"></span>{/if}
				Интерполировать
			</button>
		</aside>

		<main class="chart-area">
			<div class="chart-card">
				<div class="chart-meta">
					<span class="chart-label">График</span>
				</div>
				<div class="plot-box" bind:this={plotContainer}></div>
			</div>
			{#if errorMsg}
				<div class="error-bar" in:slide={{ duration: 180 }}>{errorMsg}</div>
			{/if}
		</main>

		<aside class="results-panel">
			<div class="results-header">
				<span class="field-label">Результаты</span>
				{#if currentModeData.diffTable && currentModeData.diffTable.length > 0}
					<button class="btn-diff" onclick={() => isTableModalOpen = true}>Разностная таблица</button>
				{/if}
			</div>

			<div class="results-list">
				{#if currentModeData.results.length === 0}
					<div class="results-empty">
						<p>Задайте узлы и нажмите «Интерполировать»</p>
					</div>
				{:else}
					{#each currentModeData.results as res}
						{@const color = COLORS[res.type.id % COLORS.length]}
						{@const visible = currentModeData.visibleMethods.has(res.type.id)}
						{@const failed = res.y === null}
						<div
							class="result-card"
							class:is-muted={!visible}
							class:is-failed={failed}
							style="--c: {color}"
						>
							<div class="rc-head">
								<div class="rc-title">
									<span class="rc-dot" style="background:{color}"></span>
									<strong>{res.type.label}</strong>
								</div>
								<label class="rc-toggle" title="Показать / скрыть">
									<input
										type="checkbox"
										checked={visible}
										onclick={() => {
											if (currentModeData.visibleMethods.has(res.type.id)) currentModeData.visibleMethods.delete(res.type.id);
											else currentModeData.visibleMethods.add(res.type.id);
											currentModeData.visibleMethods = new Set(currentModeData.visibleMethods);
										}}
									/>
									<span class="toggle-track"><span class="toggle-thumb"></span></span>
								</label>
							</div>

							{#if res.y !== null}
								<div class="rc-value">
									<span class="rc-eq">y({res.x})</span>
									<span class="rc-num" style="color:{color}">{res.y.toFixed(6)}</span>
								</div>
							{:else}
								<div class="rc-na">Не удалось вычислить</div>
							{/if}

							{#if res.message}
								<div class="rc-msg" class:rc-msg-warn={failed}>{res.message}</div>
							{/if}
						</div>
					{/each}
				{/if}
			</div>
		</aside>
	</div>
</div>

{#if isTableModalOpen}
	<!-- svelte-ignore a11y_click_events_have_key_events -->
	<!-- svelte-ignore a11y_no_static_element_interactions -->
	<div class="modal-bg" onclick={() => isTableModalOpen = false} in:fade={{ duration: 150 }}>
		<div class="modal" onclick={(e) => e.stopPropagation()}>
			<div class="modal-head">
				<h3>Таблица конечных разностей</h3>
				<button onclick={() => isTableModalOpen = false}>×</button>
			</div>
			<div class="modal-body">
				<table>
					<thead>
						<tr>
							<th>X</th>
							<th>y</th>
							{#each currentModeData.diffTable as col, i}
								{#if i > 0}<th>Δ<sup>{i}</sup>y</th>{/if}
							{/each}
						</tr>
					</thead>
					<tbody>
						{#each Array(currentModeData.points.length) as _, rowIndex}
							<tr>
								<td>{currentModeData.points[rowIndex].x.toFixed(3)}</td>
								{#each currentModeData.diffTable as col}
									<td>{col[rowIndex] !== undefined && col[rowIndex] !== null ? col[rowIndex].toFixed(3) : ''}</td>
								{/each}
							</tr>
						{/each}
					</tbody>
				</table>
			</div>
		</div>
	</div>
{/if}

<style>
	@import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Mono:wght@400;500&family=IBM+Plex+Sans:wght@400;500;600;700&display=swap');

	:global(*) { box-sizing: border-box; }
	:global(body) {
		margin: 0;
		font-family: 'IBM Plex Sans', system-ui, sans-serif;
		background: #f8f9fb;
		color: #111827;
		overflow: hidden;
		-webkit-font-smoothing: antialiased;
	}
	:global(.function-plot .annotations path) {
		stroke: #10b981 !important;
		stroke-width: 1.5px !important;
		stroke-dasharray: 4 3 !important;
	}
	:global(.function-plot .annotations text) {
		fill: #059669 !important;
		font-size: 11px !important;
		font-weight: 600 !important;
		font-family: 'IBM Plex Mono', monospace !important;
	}

	.app { display: flex; flex-direction: column; height: 100vh; }

	.header {
		height: 52px;
		background: #fff;
		border-bottom: 1px solid #e5e7eb;
		padding: 0 1.5rem;
		display: flex;
		align-items: center;
		justify-content: space-between;
		flex-shrink: 0;
	}
	.header-left { display: flex; align-items: center; gap: 12px; }
	.logo {
		width: 32px; height: 32px;
		background: #111827;
		color: #fff;
		border-radius: 6px;
		display: flex; align-items: center; justify-content: center;
		font-size: 0.65rem; font-weight: 700;
		font-family: 'IBM Plex Mono', monospace;
		letter-spacing: -0.5px;
	}
	.title-main { display: block; font-size: 0.875rem; font-weight: 700; line-height: 1.1; color: #111827; }
	.title-sub { display: block; font-size: 0.625rem; color: #9ca3af; font-weight: 400; }
	.author { font-size: 0.75rem; color: #9ca3af; font-weight: 500; }

	.layout {
		display: grid;
		grid-template-columns: 256px 1fr 320px;
		flex: 1;
		overflow: hidden;
	}

	.sidebar {
		background: #fff;
		border-right: 1px solid #e5e7eb;
		display: flex;
		flex-direction: column;
		padding: 14px 12px;
		gap: 10px;
		overflow: hidden;
	}

	/* X input */
	.x-box {
		display: flex;
		align-items: center;
		gap: 8px;
		background: #f0fdf4;
		border: 1px solid #bbf7d0;
		border-radius: 8px;
		padding: 8px 12px;
		flex-shrink: 0;
	}
	.x-label {
		font-family: 'IBM Plex Mono', monospace;
		font-size: 0.9rem;
		font-weight: 600;
		color: #166534;
		white-space: nowrap;
	}
	.x-input {
		flex: 1;
		min-width: 0;
		border: none;
		background: transparent;
		font-family: 'IBM Plex Mono', monospace;
		font-size: 1rem;
		font-weight: 600;
		color: #166534;
		text-align: right;
		outline: none;
	}

	/* Tabs */
	.tabs {
		display: flex;
		background: #f3f4f6;
		border-radius: 7px;
		padding: 3px;
		flex-shrink: 0;
	}
	.tabs button {
		flex: 1;
		background: none;
		border: none;
		padding: 6px 4px;
		border-radius: 5px;
		font-family: 'IBM Plex Sans', sans-serif;
		font-size: 0.7rem;
		font-weight: 600;
		color: #6b7280;
		cursor: pointer;
		transition: all 0.15s;
	}
	.tabs button.active {
		background: #fff;
		color: #111827;
		box-shadow: 0 1px 3px rgba(0,0,0,0.08);
	}

	/* File zone */
	.file-zone { flex-shrink: 0; }
	.btn-file {
		width: 100%;
		background: #f9fafb;
		border: 1.5px dashed #d1d5db;
		border-radius: 7px;
		padding: 9px;
		color: #6b7280;
		font-family: 'IBM Plex Sans', sans-serif;
		font-size: 0.72rem;
		font-weight: 600;
		cursor: pointer;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 6px;
		transition: all 0.15s;
	}
	.btn-file:hover { border-color: #9ca3af; color: #374151; background: #f3f4f6; }
	.file-hint { margin: 6px 0 0; font-size: 0.68rem; color: #9ca3af; text-align: center; font-style: italic; }

	/* Function panel */
	.func-panel {
		background: #f9fafb;
		border: 1px solid #e5e7eb;
		border-radius: 8px;
		padding: 10px;
		flex-shrink: 0;
	}
	.field-label {
		display: block;
		font-size: 0.6rem;
		font-weight: 700;
		text-transform: uppercase;
		letter-spacing: 0.08em;
		color: #9ca3af;
		margin-bottom: 6px;
	}
	.func-select {
		width: 100%;
		background: #fff;
		border: 1px solid #e5e7eb;
		border-radius: 6px;
		padding: 6px 8px;
		font-family: 'IBM Plex Mono', monospace;
		font-size: 0.75rem;
		color: #111827;
		outline: none;
		cursor: pointer;
		margin-bottom: 8px;
	}
	.func-params { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 6px; }
	.param-group { display: flex; flex-direction: column; gap: 3px; }
	.param-group label {
		font-size: 0.6rem;
		font-weight: 700;
		text-transform: uppercase;
		letter-spacing: 0.08em;
		color: #9ca3af;
		text-align: center;
	}
	.param-group input {
		width: 100%;
		background: #fff;
		border: 1px solid #e5e7eb;
		border-radius: 5px;
		padding: 5px 4px;
		font-family: 'IBM Plex Mono', monospace;
		font-size: 0.75rem;
		color: #111827;
		text-align: center;
		outline: none;
	}
	.param-group input:focus { border-color: #6b7280; }
	.func-error { margin: 6px 0 0; font-size: 0.68rem; color: #dc2626; font-weight: 500; text-align: center; }

	/* Points */
	.points-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		flex-shrink: 0;
	}
	.btn-add {
		background: none;
		border: 1px solid #e5e7eb;
		border-radius: 5px;
		padding: 3px 9px;
		font-family: 'IBM Plex Sans', sans-serif;
		font-size: 0.65rem;
		font-weight: 600;
		color: #374151;
		cursor: pointer;
		transition: all 0.15s;
	}
	.btn-add:hover:not(:disabled) { background: #f3f4f6; border-color: #9ca3af; }
	.btn-add:disabled { opacity: 0.35; cursor: not-allowed; }

	.points-list {
		flex: 1;
		overflow-y: auto;
		display: flex;
		flex-direction: column;
		gap: 3px;
		min-height: 0;
		padding-right: 2px;
	}
	.points-list::-webkit-scrollbar { width: 3px; }
	.points-list::-webkit-scrollbar-thumb { background: #e5e7eb; border-radius: 3px; }

	.point-row {
		display: flex;
		align-items: center;
		gap: 6px;
		padding: 5px 8px;
		border-radius: 6px;
		border: 1px solid transparent;
		cursor: pointer;
		transition: all 0.12s;
		background: #f9fafb;
	}
	.point-row:hover:not(.readonly) { background: #f3f4f6; border-color: #e5e7eb; }
	.point-row.is-active { background: #fff1f2; border-color: #fca5a5; }
	.point-row.readonly { cursor: default; background: #f9fafb; }
	.point-idx {
		font-family: 'IBM Plex Mono', monospace;
		font-size: 0.6rem;
		font-weight: 500;
		color: #9ca3af;
		width: 14px;
		flex-shrink: 0;
		text-align: right;
	}
	.point-fields { display: flex; gap: 4px; flex: 1; }
	.point-fields input {
		width: 100%;
		border: none;
		background: transparent;
		font-family: 'IBM Plex Mono', monospace;
		font-size: 0.75rem;
		color: #374151;
		padding: 1px 3px;
		outline: none;
		min-width: 0;
	}
	.point-fields input:focus { color: #111827; }
	.point-fields input:disabled { color: #6b7280; cursor: default; }
	.btn-remove {
		background: none;
		border: none;
		color: #d1d5db;
		font-size: 1rem;
		line-height: 1;
		cursor: pointer;
		padding: 0;
		transition: color 0.12s;
		flex-shrink: 0;
	}
	.btn-remove:hover:not(:disabled) { color: #ef4444; }
	.btn-remove:disabled { opacity: 0.25; cursor: not-allowed; }

	/* Run button */
	.btn-run {
		width: 100%;
		background: #111827;
		color: #fff;
		border: none;
		border-radius: 8px;
		padding: 11px;
		font-family: 'IBM Plex Sans', sans-serif;
		font-size: 0.8rem;
		font-weight: 600;
		cursor: pointer;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 7px;
		flex-shrink: 0;
		transition: all 0.15s;
	}
	.btn-run:hover:not(:disabled) { background: #1f2937; }
	.btn-run:disabled { background: #e5e7eb; color: #9ca3af; cursor: not-allowed; }
	.spinner {
		width: 12px; height: 12px;
		border: 2px solid rgba(255,255,255,0.25);
		border-top-color: #fff;
		border-radius: 50%;
		animation: spin 0.75s linear infinite;
	}
	@keyframes spin { to { transform: rotate(360deg); } }

	/* ── CHART ────────────────────────────────────── */
	.chart-area {
		background: #f3f4f6;
		padding: 14px;
		display: flex;
		flex-direction: column;
		gap: 10px;
		overflow: hidden;
	}
	.chart-card {
		flex: 1;
		background: #fff;
		border: 1px solid #e5e7eb;
		border-radius: 10px;
		display: flex;
		flex-direction: column;
		overflow: hidden;
		min-height: 0;
	}
	.chart-meta {
		padding: 10px 14px;
		border-bottom: 1px solid #f3f4f6;
		display: flex;
		align-items: center;
		justify-content: space-between;
		flex-shrink: 0;
	}
	.chart-label {
		font-size: 0.6rem;
		font-weight: 700;
		text-transform: uppercase;
		letter-spacing: 0.08em;
		color: #9ca3af;
	}
	.chart-hint {
		font-size: 0.65rem;
		color: #9ca3af;
		font-style: italic;
	}
	.plot-box {
		flex: 1;
		overflow: hidden;
		min-height: 0;
	}
	:global(.plot-box svg) { display: block; }

	.error-bar {
		background: #fef2f2;
		border: 1px solid #fecaca;
		border-radius: 7px;
		padding: 8px 14px;
		font-size: 0.75rem;
		font-weight: 500;
		color: #dc2626;
		flex-shrink: 0;
	}

	/* ── RESULTS (RIGHT) ──────────────────────────── */
	.results-panel {
		background: #fff;
		border-left: 1px solid #e5e7eb;
		display: flex;
		flex-direction: column;
		padding: 14px 12px;
		gap: 10px;
		overflow: hidden;
	}
	.results-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		flex-shrink: 0;
	}
	.btn-diff {
		background: none;
		border: 1px solid #e5e7eb;
		border-radius: 5px;
		padding: 3px 9px;
		font-family: 'IBM Plex Sans', sans-serif;
		font-size: 0.65rem;
		font-weight: 600;
		color: #374151;
		cursor: pointer;
		transition: all 0.15s;
	}
	.btn-diff:hover { background: #f3f4f6; border-color: #9ca3af; }

	.results-list {
		flex: 1;
		overflow-y: auto;
		display: flex;
		flex-direction: column;
		gap: 6px;
		min-height: 0;
	}
	.results-list::-webkit-scrollbar { width: 3px; }
	.results-list::-webkit-scrollbar-thumb { background: #e5e7eb; border-radius: 3px; }

	.results-empty {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		gap: 10px;
		padding: 2rem;
	}
	.results-empty p { font-size: 0.75rem; color: #9ca3af; text-align: center; margin: 0; line-height: 1.5; }

	/* Result card */
	.result-card {
		border: 1px solid #e5e7eb;
		border-left: 3px solid var(--c);
		border-radius: 8px;
		padding: 10px 12px;
		background: #fff;
		transition: opacity 0.18s, filter 0.18s;
	}
	.result-card.is-muted { opacity: 0.38; filter: grayscale(0.6); }
	.result-card.is-failed { background: #fef9f9; border-color: #fecaca; border-left-color: #fca5a5; }

	.rc-head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 8px;
	}
	.rc-title { display: flex; align-items: center; gap: 7px; }
	.rc-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
	.rc-title strong { font-size: 0.72rem; font-weight: 600; color: #374151; line-height: 1.3; }

	/* Toggle */
	.rc-toggle { cursor: pointer; }
	.rc-toggle input { display: none; }
	.toggle-track {
		display: block;
		width: 28px; height: 16px;
		background: #e5e7eb;
		border-radius: 8px;
		position: relative;
		transition: background 0.2s;
	}
	.rc-toggle input:checked ~ .toggle-track { background: var(--c); }
	.toggle-thumb {
		position: absolute;
		top: 2px; left: 2px;
		width: 12px; height: 12px;
		background: #fff;
		border-radius: 50%;
		box-shadow: 0 1px 2px rgba(0,0,0,0.15);
		transition: left 0.18s;
	}
	.rc-toggle input:checked ~ .toggle-track .toggle-thumb { left: 14px; }

	.rc-value {
		display: flex;
		align-items: baseline;
		gap: 6px;
		font-family: 'IBM Plex Mono', monospace;
	}
	.rc-eq { font-size: 0.68rem; color: #9ca3af; }
	.rc-num { font-size: 1rem; font-weight: 500; }
	.rc-na { font-size: 0.7rem; color: #9ca3af; font-style: italic; margin-top: 2px; }
	.rc-msg {
		margin-top: 7px;
		font-size: 0.65rem;
		color: #6b7280;
		background: #f9fafb;
		border: 1px solid #f3f4f6;
		border-radius: 5px;
		padding: 5px 8px;
		line-height: 1.45;
	}
	.rc-msg.rc-msg-warn { background: #fffbeb; border-color: #fde68a; color: #92400e; }

	/* ── MODAL ────────────────────────────────────── */
	.modal-bg {
		position: fixed; inset: 0;
		background: rgba(0,0,0,0.25);
		backdrop-filter: blur(2px);
		display: flex; align-items: center; justify-content: center;
		z-index: 100;
	}
	.modal {
		background: #fff;
		border-radius: 12px;
		width: 90%; max-width: 720px; max-height: 80vh;
		display: flex; flex-direction: column;
		box-shadow: 0 16px 32px rgba(0,0,0,0.1), 0 0 0 1px rgba(0,0,0,0.04);
	}
	.modal-head {
		padding: 14px 18px;
		border-bottom: 1px solid #e5e7eb;
		display: flex; align-items: center; justify-content: space-between;
		flex-shrink: 0;
	}
	.modal-head h3 { margin: 0; font-size: 0.875rem; font-weight: 600; color: #111827; }
	.modal-head button {
		background: none; border: none;
		font-size: 1.25rem; line-height: 1;
		color: #9ca3af; cursor: pointer;
		transition: color 0.12s;
		padding: 0;
	}
	.modal-head button:hover { color: #374151; }
	.modal-body { padding: 14px; overflow: auto; }

	table { width: 100%; border-collapse: collapse; font-size: 0.75rem; text-align: center; }
	thead th {
		background: #f9fafb;
		color: #6b7280;
		font-family: 'IBM Plex Mono', monospace;
		font-weight: 600;
		padding: 8px 10px;
		border-bottom: 1px solid #e5e7eb;
		position: sticky; top: 0;
	}
	tbody td {
		padding: 6px 10px;
		color: #374151;
		font-family: 'IBM Plex Mono', monospace;
		border-bottom: 1px solid #f3f4f6;
	}
	tbody tr:last-child td { border-bottom: none; }
	tbody tr:hover td { background: #f9fafb; }
</style>