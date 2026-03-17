<script lang="ts">
	import { onMount } from 'svelte';
	import functionPlot from 'function-plot';
	import { fade, slide } from 'svelte/transition';

	// --- Интерфейсы ---
	interface Equation { id: number; formula: string; view: string; }
	interface Method { id: number; label: string; }
	interface System { id: number; eq1: { formula: string; view: string }; eq2: { formula: string; view: string }; }
	interface SolveResponse {
		root?: number; funcValue?: number;
		x?: number; y?: number; dx?: number; dy?: number;
		iterations: number; error: string | null;
	}

	//const API_BASE = 'http://localhost:8080/lab2/api';
    const API_BASE = 'https://comp-math.arhr.tech/lab2/api';

	// --- Состояние ---
	let equations = $state<Equation[]>([]);
	let methods = $state<Method[]>([]);
	let systems = $state<System[]>([]);

	let mode = $state<'single' | 'system'>('single');
	let selectedSingleId = $state<number>(0);
	let selectedMethodId = $state<number>(0);
	let selectedSystemId = $state<number>(0);

	let a = $state<number>(-10);
	let b = $state<number>(-9);
	let x0 = $state<number>(1);
	let y0 = $state<number>(1);
	let eps = $state<number>(0.000001);

	let result = $state<SolveResponse | null>(null);
	let isLoading = $state<boolean>(false);
	let errorMsg = $state<string | null>(null);

	let plotContainer: HTMLDivElement | undefined = $state();

	onMount(async () => {
		try {
			const [eqRes, methRes, sysRes] = await Promise.all([
				fetch(`${API_BASE}/equations/all`),
				fetch(`${API_BASE}/equations/methods`),
				fetch(`${API_BASE}/systems/all`)
			]);
			equations = await eqRes.json();
			methods = await methRes.json();
			systems = await sysRes.json();
		} catch (e) {
			errorMsg = "Сервер недоступен.";
		}
	});

	$effect(() => {
		if (plotContainer && (equations.length > 0 || systems.length > 0)) {
			drawPlot();
		}
	});

	function drawPlot(): void {
		if (!plotContainer) return;
		try {
			let data: any[] = [];
			let annotations: any[] = [];

			if (mode === 'single') {
				const eq = equations.find(e => e.id === selectedSingleId);
				if (eq) {
					data.push({ fn: eq.formula, color: '#6366f1', nSamples: 10000 });
					const start = Math.min(a, b);
					const end = Math.max(a, b);
					data.push({ fn: '2000', range: [start, end], closed: true, color: 'rgba(99, 102, 241, 0.3)', skipTip: true });
					data.push({ fn: '-2000', range: [start, end], closed: true, color: 'rgba(99, 102, 241, 0.3)', skipTip: true });
					annotations = [{ x: a, text: `a` }, { x: b, text: `b` }];
				}
			} else {
				const sys = systems.find(s => s.id === selectedSystemId);
				if (sys) {
					data = [
						{ fn: sys.eq1.formula, fnType: 'implicit', color: '#6366f1' },
						{ fn: sys.eq2.formula, fnType: 'implicit', color: '#10b981' }
					];
					annotations = [{ x: x0, text: 'x0' }, { y: y0, text: 'y0' }];
				}
			}

			functionPlot({
				target: plotContainer,
				width: 480,
				height: 380,
				grid: true,
				xAxis: { domain: [-12, 12] },
				yAxis: { domain: [-12, 12] },
				data,
				annotations
			});
		} catch (err) { console.error(err); }
	}

	async function handleSubmit() {
		isLoading = true; errorMsg = null; result = null;
		const url = mode === 'single' ? `${API_BASE}/equations/solve` : `${API_BASE}/systems/solve`;
		const payload = mode === 'single' 
			? { equationId: selectedSingleId, methodId: selectedMethodId, a, b, eps }
			: { systemId: selectedSystemId, x0, y0, eps };

		try {
			const res = await fetch(url, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(payload)
			});
			const data = await res.json();
			if (data.error) errorMsg = data.error;
			else result = data;
		} catch { errorMsg = "Ошибка сети"; }
		finally { isLoading = false; }
	}
</script>

<div class="app-shell">
	<aside class="sidebar">
		<div class="brand">
			<div class="logo">Σ</div>
			<div class="brand-text">
				<h2>CompMath</h2>
				<span>Lab 2 • Храбров Артём</span>
			</div>
		</div>

		<nav class="mode-switch">
			<button class:active={mode === 'single'} onclick={() => mode = 'single'}>Уравнение</button>
			<button class:active={mode === 'system'} onclick={() => mode = 'system'}>Система</button>
		</nav>

		<div class="config-scroll">
			<section>
				<label class="section-title">Выбор</label>
				<div class="selection-list">
					{#if mode === 'single'}
						{#each equations as eq}
							<button class="choice-card" class:selected={selectedSingleId === eq.id} onclick={() => selectedSingleId = eq.id}>
								<span class="formula">{eq.view} = 0</span>
							</button>
						{/each}
					{:else}
						{#each systems as sys}
							<button class="choice-card" class:selected={selectedSystemId === sys.id} onclick={() => selectedSystemId = sys.id}>
								<div class="sys-bracket">
									<div class="sys-stack">
										<span>{sys.eq1.view} = 0</span>
										<span>{sys.eq2.view} = 0</span>
									</div>
								</div>
							</button>
						{/each}
					{/if}
				</div>
			</section>

			<section>
				<label class="section-title">Метод решения</label>
				{#if mode === 'single'}
					<div class="custom-select-wrapper">
						<select bind:value={selectedMethodId} class="styled-select">
							{#each methods as m}
								<option value={m.id}>{m.label}</option>
							{/each}
						</select>
					</div>
				{:else}
					<div class="method-badge">Метод Ньютона</div>
				{/if}
			</section>

			<section>
				<label class="section-title">Параметры</label>
				<div class="grid-inputs">
					{#if mode === 'single'}
						<div class="input-box"><span>a</span><input type="number" step="0.1" bind:value={a}></div>
						<div class="input-box"><span>b</span><input type="number" step="0.1" bind:value={b}></div>
					{:else}
						<div class="input-box"><span>x₀</span><input type="number" step="0.1" bind:value={x0}></div>
						<div class="input-box"><span>y₀</span><input type="number" step="0.1" bind:value={y0}></div>
					{/if}
					<div class="input-box wide"><span>ε (точность)</span><input type="number" step="0.000001" bind:value={eps}></div>
				</div>
			</section>
		</div>

		<button class="primary-btn" onclick={handleSubmit} disabled={isLoading}>
			{#if isLoading} <span class="spinner"></span> {/if}
			{isLoading ? 'Вычисляем...' : 'Решить задачу'}
		</button>
	</aside>

	<main class="content">
		<div class="viz-card">
			<div class="viz-header">
				<h3>Графическая визуализация</h3>
				<div class="legend">
					<span class="l-item"><i style="background: #6366f1"></i> {mode === 'single' ? 'f(x)' : 'Eq 1'}</span>
					{#if mode === 'system'}<span class="l-item"><i style="background: #10b981"></i> Eq 2</span>{/if}
				</div>
			</div>
			<div class="plot-wrapper" bind:this={plotContainer}></div>
		</div>

		{#if errorMsg}
			<div class="msg-card error" in:slide>
				<div class="msg-icon"><b>!</b></div>
				<div class="msg-body">
					<p>{errorMsg}</p>
				</div>
			</div>
		{/if}

		<!-- Находим блок результатов в твоем коде и заменяем вывод значений -->
{#if result}
    <div class="msg-card success" in:slide>
        <div class="msg-header">
            <h4>Результаты</h4>
            <span class="iter-tag">{result.iterations} итераций</span>
        </div>
        <div class="res-grid">
            {#if mode === 'single'}
                <!-- Убрали .toFixed(6) и .toExponential(2) -->
                <div class="res-item"><span>Корень</span><strong>{result.root}</strong></div>
                <div class="res-item"><span>f(x)</span><strong>{result.funcValue}</strong></div>
            {:else}
                <!-- Убрали .toFixed(4) -->
                <div class="res-item"><span>X</span><strong>{result.x}</strong></div>
                <div class="res-item"><span>Y</span><strong>{result.y}</strong></div>
                <div class="res-item"><span>Δx</span><strong>{result.dx ?? '—'}</strong></div>
                <div class="res-item"><span>Δy</span><strong>{result.dy ?? '—'}</strong></div>
            {/if}
        </div>
    </div>
{/if}
	</main>
</div>

<style>
	:global(*) { box-sizing: border-box; }
	:global(body) { margin: 0; font-family: 'Inter', sans-serif; background: #f8fafc; overflow: hidden; }
	
	.app-shell { display: flex; height: 100vh; width: 100vw; }

	/* Sidebar */
	.sidebar { 
		width: 360px; min-width: 360px; background: #fff; border-right: 1px solid #e2e8f0; 
		display: flex; flex-direction: column; padding: 1.25rem; overflow-x: hidden;
	}
	.brand { display: flex; align-items: center; gap: 10px; margin-bottom: 1.5rem; }
	.logo { background: #6366f1; color: white; min-width: 36px; height: 36px; border-radius: 8px; display: grid; place-items: center; font-weight: 800; }
	.brand h2 { margin: 0; font-size: 1rem; }
	.brand span { font-size: 0.7rem; color: #94a3b8; }
	
	.mode-switch { display: flex; background: #f1f5f9; padding: 3px; border-radius: 10px; margin-bottom: 1.25rem; }
	.mode-switch button { flex: 1; border: none; padding: 8px; border-radius: 7px; font-weight: 600; cursor: pointer; color: #64748b; background: transparent; font-size: 0.85rem; }
	.mode-switch button.active { background: white; color: #6366f1; box-shadow: 0 2px 6px rgba(0,0,0,0.05); }
	
	.config-scroll { flex: 1; overflow-y: auto; padding-right: 4px; }
	section { margin-bottom: 1.25rem; }
	.section-title { display: block; font-size: 0.7rem; text-transform: uppercase; font-weight: 700; color: #94a3b8; margin-bottom: 0.6rem; letter-spacing: 0.05em; }

	/* Choice Card & Bracket */
	.choice-card { text-align: left; padding: 10px; border: 2px solid #f1f5f9; border-radius: 10px; background: white; cursor: pointer; width: 100%; margin-bottom: 5px; transition: 0.2s; }
	.choice-card.selected { border-color: #6366f1; background: #f5f3ff; }
	.formula { font-family: serif; font-size: 0.9rem; }
	
	.sys-bracket { display: flex; align-items: center; gap: 8px; }
	.sys-bracket::before { 
		content: '{'; font-family: 'Cambria', serif; font-size: 2.2rem; font-weight: 300; 
		color: #6366f1; margin-top: -4px; line-height: 1;
	}
	.sys-stack { display: flex; flex-direction: column; font-size: 0.85rem; color: #475569; line-height: 1.3; font-family: serif; }

	/* Styled Select */
	.custom-select-wrapper { position: relative; width: 100%; }
	.custom-select-wrapper::after {
		content: ''; position: absolute; right: 12px; top: 50%; transform: translateY(-50%);
		width: 10px; height: 6px; background-color: #64748b;
		clip-path: polygon(100% 0%, 0 0%, 50% 100%); pointer-events: none;
	}
	.styled-select { 
		width: 100%; padding: 10px 30px 10px 12px; border-radius: 8px; border: 1px solid #cbd5e1; 
		background: #f8fafc; font-weight: 600; font-size: 0.9rem; color: #1e293b;
		appearance: none; cursor: pointer; transition: all 0.2s ease;
	}
	.styled-select:focus { border-color: #6366f1; background: #fff; box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1); outline: none; }

	.grid-inputs { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
	.input-box { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 6px 10px; transition: 0.2s; }
	.input-box:focus-within { border-color: #6366f1; background: white; }
	.input-box span { font-size: 0.65rem; font-weight: 700; color: #94a3b8; }
	.input-box input { border: none; background: transparent; font-weight: 600; width: 100%; outline: none; font-size: 0.9rem; }
	
	.method-badge { background: #e0e7ff; color: #4338ca; padding: 10px; border-radius: 8px; font-weight: 700; text-align: center; font-size: 0.85rem; }
	.primary-btn { width: 100%; background: #6366f1; color: white; border: none; padding: 14px; border-radius: 10px; font-weight: 700; cursor: pointer; margin-top: 10px; transition: 0.2s; }
	.primary-btn:hover { background: #4f46e5; transform: translateY(-1px); }

	/* Content & Results */
	.content { flex: 1; padding: 1rem; display: flex; flex-direction: column; gap: 0.75rem; align-items: center; justify-content: center; }
	.viz-card { background: white; padding: 1rem; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; }
	.viz-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem; }
	.viz-header h3 { margin: 0; font-size: 0.9rem; color: #475569; }
	.legend { display: flex; gap: 12px; font-size: 0.75rem; font-weight: 600; color: #64748b; }
	.l-item { display: flex; align-items: center; gap: 4px; }
	.l-item i { width: 7px; height: 7px; border-radius: 50%; display: block; }

	.msg-card { width: 100%; max-width: 500px; padding: 0.85rem 1.25rem; border-radius: 12px; display: flex; gap: 0.75rem; box-shadow: 0 2px 10px rgba(0,0,0,0.02); }
	.msg-card.success { background: #f0fdf4; border: 1px solid #bbf7d0; flex-direction: column; color: #166534; }
	.msg-card.error { background: #fff1f2; border: 1px solid #fecdd3; color: #9f1239; align-items: center; }
	.msg-icon { background: #fb7185; color: white; min-width: 24px; height: 24px; border-radius: 50%; display: grid; place-items: center; font-weight: 800; font-size: 0.8rem; }
	
	.msg-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
	.iter-tag { background: rgba(0,0,0,0.04); padding: 2px 8px; border-radius: 12px; font-size: 0.7rem; font-weight: 700; }
	.res-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(100px, 1fr)); gap: 0.75rem; border-top: 1px solid rgba(0,0,0,0.04); padding-top: 0.75rem; }
	.res-item { display: flex; flex-direction: column; }
	.res-item span { font-size: 0.65rem; text-transform: uppercase; font-weight: 700; opacity: 0.6; }
	.res-item strong { font-size: 0.95rem; font-family: monospace; }

	.spinner { width: 14px; height: 14px; border: 2px solid rgba(255,255,255,0.3); border-top-color: white; border-radius: 50%; animation: spin 0.8s linear infinite; }
	@keyframes spin { to { transform: rotate(360deg); } }
</style>