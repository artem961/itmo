<script lang="ts">
	import { onMount } from 'svelte';
	import functionPlot from 'function-plot';
	import { fade, slide } from 'svelte/transition';

	// --- Интерфейсы под Lab 3 ---
	interface Equation { id: number; formula: string; view: string; }
	interface Method { id: number; label: string; }
	
	interface SolveResponse {
		value?: string;   
		splits?: number;  
		error: string | null;
	}

	const API_BASE = 'https://comp-math.arhr.tech/lab3/api/integrals';

	//const API_BASE = 'http://localhost:8080/lab3/api/integrals';

	let equations = $state<Equation[]>([]);
	let methods = $state<Method[]>([]);

	let selectedEquationId = $state<number>(0);
	let selectedMethodId = $state<number>(0);

	let a = $state<number>(0);
	let b = $state<number>(2);
	let eps = $state<number>(0.01);

	let result = $state<SolveResponse | null>(null);
	let isLoading = $state<boolean>(false);
	let errorMsg = $state<string | null>(null);

	let plotContainer: HTMLDivElement | undefined = $state();

	function extractRaw(jsonStr: string, key: string): string | undefined {
		const regex = new RegExp(`"${key}"\\s*:\\s*([^,}]+)`);
		const match = jsonStr.match(regex);
		if (!match) return undefined;
		let val = match[1].trim().replace(/^"|"$/g, ''); 
		if (val === 'null') return undefined;
		return val.length > 15 ? val.substring(0, 15) : val;
	}

	onMount(async () => {
		try {
			const [eqRes, methRes] = await Promise.all([
				fetch(`${API_BASE}/equations`),
				fetch(`${API_BASE}/methods`)
			]);
			equations = await eqRes.json();
			methods = await methRes.json();
		} catch (e) {
			errorMsg = "Сервер недоступен.";
		}
	});

	$effect(() => {
		if (plotContainer && equations.length > 0) {
			drawPlot();
		}
	});

	function drawPlot(): void {
		if (!plotContainer) return;
		try {
			let data: any[] = [];
			let annotations: any[] = [];

			const eq = equations.find(e => e.id === selectedEquationId);
			if (eq) {
				data.push({ 
					fn: eq.formula, 
					color: '#6366f1', 
					nSamples: 1000 
				});

				const rangeStart = Math.min(a, b);
				const rangeEnd = Math.max(a, b);
				
				data.push({
					fn: eq.formula,
					range: [rangeStart, rangeEnd],
					closed: true,
					color: 'rgba(50, 20, 200, 0.5)',
					skipTip: true,
					nSamples: 1000
				});

				annotations = [
					{ x: a, text: `a=${a}` }, 
					{ x: b, text: `b=${b}` }
				];
			}

			functionPlot({
				target: plotContainer,
				width: 480,
				height: 380,
				grid: true,
				xAxis: { domain: [-6, 6] },
				yAxis: { domain: [-10, 20] },
				data,
				annotations
			});
		} catch (err) { console.error(err); }
	}

	async function handleSubmit() {
		isLoading = true; errorMsg = null; result = null;
		const payload = { equationId: selectedEquationId, methodId: selectedMethodId, a, b, eps };
		try {
			const res = await fetch(`${API_BASE}/solve`, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(payload)
			});
			const rawText = await res.text();
			const data = JSON.parse(rawText);
			if (data.error) errorMsg = data.error;
			else {
				result = {
					splits: data.splits,
					error: data.error,
					value: extractRaw(rawText, 'value')
				};
			}
		} catch { errorMsg = "Ошибка сети"; }
		finally { isLoading = false; }
	}
</script>

<div class="app-shell">
	<!-- Добавлен динамический класс simpson-bg -->
	<aside class="sidebar" class:simpson-bg={selectedMethodId === 4}>
		<div class="brand">
			<div class="logo">∫</div>
			<div class="brand-text">
				<h2>CompMath</h2>
				<span>Lab 3 • Храбров Артём</span>
			</div>
		</div>

		<div class="config-scroll">
			<section>
				<label class="section-title">Выбор функции</label>
				<div class="selection-list">
					{#each equations as eq}
						<button class="choice-card" class:selected={selectedEquationId === eq.id} onclick={() => selectedEquationId = eq.id}>
							<div class="integral-display">
								<div class="int-symbol">
									<span class="int-limit-top">{b}</span>
									<span class="int-sign">∫</span>
									<span class="int-limit-bottom">{a}</span>
								</div>
								<span class="formula">{eq.view} dx</span>
							</div>
						</button>
					{/each}
				</div>
			</section>

			<section>
				<label class="section-title">Метод решения</label>
				<div class="custom-select-wrapper">
					<select bind:value={selectedMethodId} class="styled-select">
						{#each methods as m}
							<option value={m.id}>{m.label}</option>
						{/each}
					</select>
				</div>
			</section>

			<section>
				<label class="section-title">Параметры интеграла</label>
				<div class="grid-inputs">
					<div class="input-box"><span>a</span><input type="number" step="0.1" bind:value={a}></div>
					<div class="input-box"><span>b</span><input type="number" step="0.1" bind:value={b}></div>
					<div class="input-box wide"><span>ε</span><input type="number" step="0.000001" bind:value={eps}></div>
				</div>
			</section>
		</div>

		<button class="primary-btn" onclick={handleSubmit} disabled={isLoading}>
			{#if isLoading} <span class="spinner"></span> {/if}
			{isLoading ? 'Вычисление...' : 'Вычислить интеграл'}
		</button>
	</aside>

	<main class="content">
		<div class="viz-card">
			<div class="viz-header">
				<h3>Геометрическая интерпретация</h3>
				<div class="legend">
					<span class="l-item"><i style="background: #6366f1"></i> f(x)</span>
					<span class="l-item"><i style="background: rgba(99, 102, 241, 0.4)"></i> Площадь</span>
				</div>
			</div>
			<div class="plot-wrapper" bind:this={plotContainer}></div>
		</div>

		{#if errorMsg}
			<div class="msg-card error" in:slide>
				<div class="msg-icon"><b>!</b></div>
				<div class="msg-body"><p>{errorMsg}</p></div>
			</div>
		{/if}

		{#if result}
			<div class="msg-card success" in:slide>
				<div class="msg-header">
					<h4>Итог вычислений</h4>
					<span class="iter-tag">n = {result.splits}</span>
				</div>
				<div class="res-grid">
					<div class="res-item"><span>Значение интеграла</span><strong>{result.value}</strong></div>
					<div class="res-item"><span>Число разбиений</span><strong>{result.splits}</strong></div>
				</div>
			</div>
		{/if}
	</main>
</div>


<style>
	:global(*) { box-sizing: border-box; }
	:global(body) { margin: 0; font-family: 'Inter', sans-serif; background: #f8fafc; overflow: hidden; }
	.app-shell { display: flex; height: 100vh; width: 100vw; }
	
	.sidebar { 
		width: 360px; 
		min-width: 360px; 
		background: #fff; 
		border-right: 1px solid #e2e8f0; 
		display: flex; 
		flex-direction: column; 
		padding: 1.25rem; 
		overflow-x: hidden;
		transition: all 0.3s ease; 
	}

	/* Стиль для режима Симпсона */
	.sidebar.simpson-bg {
		background-image: url('/src/lib/assets/image.png'); 
		background-size: cover;
		background-position: center;
		background-repeat: no-repeat;
	}

	/* Эффект прозрачности: уменьшил блюр до 2.5px */
	.sidebar.simpson-bg .choice-card {
		background: rgba(255, 255, 255, 0.65); /* Чуть больше прозрачности фона */
		backdrop-filter: blur(2.5px); /* Уменьшено на 30%+: было 4px */
		border-color: rgba(226, 232, 240, 0.4);
	}

	.sidebar.simpson-bg .choice-card.selected {
		background: rgba(245, 243, 255, 0.8);
		border-color: #6366f1;
	}

	.sidebar.simpson-bg .input-box {
		background: rgba(248, 250, 252, 0.65);
		backdrop-filter: blur(2.5px);
		border-color: rgba(226, 232, 240, 0.4);
	}

	.sidebar.simpson-bg .styled-select {
		background: rgba(248, 250, 252, 0.65);
		backdrop-filter: blur(2.5px);
		border-color: rgba(226, 232, 240, 0.4);
	}

	.sidebar.simpson-bg .section-title {
		color: #0f172a; 
		text-shadow: 0px 0px 8px rgba(255,255,255,0.9);
	}

    .sidebar.simpson-bg .logo {
        background: rgba(99, 102, 241, 0.85);
    }

	.brand { display: flex; align-items: center; gap: 10px; margin-bottom: 1.5rem; }
	.logo { background: #6366f1; color: white; min-width: 36px; height: 36px; border-radius: 8px; display: grid; place-items: center; font-weight: 800; font-size: 1.2rem; }
	.brand h2 { margin: 0; font-size: 1rem; }
	.brand span { font-size: 0.7rem; color: #94a3b8; }
	.config-scroll { flex: 1; overflow-y: auto; padding-right: 4px; }
	section { margin-bottom: 1.25rem; }
	.section-title { display: block; font-size: 0.7rem; text-transform: uppercase; font-weight: 700; color: #94a3b8; margin-bottom: 0.6rem; letter-spacing: 0.05em; }
	
	.choice-card { text-align: left; padding: 17px; border: 2px solid #f1f5f9; border-radius: 10px; background: white; cursor: pointer; width: 100%; margin-bottom: 8px; transition: 0.2s; }
	.choice-card.selected { border-color: #6366f1; background: #f5f3ff; }

	.integral-display { display: flex; align-items: center; gap: 12px; font-family: serif; }
	.int-symbol { position: relative; display: inline-flex; flex-direction: column; align-items: center; line-height: 1; min-width: 25px; }
	.int-sign { font-size: 2rem; color: #6366f1; font-weight: 300; }
	.int-limit-top { font-size: 0.7rem; position: absolute; top: -14px; right: 3px; font-weight: 600; color: #1e293b; }
	.int-limit-bottom { font-size: 0.7rem; position: absolute; bottom: -10px; left: 3px; font-weight: 600; color: #1e293b; }
	.formula { font-size: 1rem; color: #334155; }

	.custom-select-wrapper { position: relative; width: 100%; }
	.custom-select-wrapper::after { content: ''; position: absolute; right: 12px; top: 50%; transform: translateY(-50%); width: 10px; height: 6px; background-color: #64748b; clip-path: polygon(100% 0%, 0 0%, 50% 100%); pointer-events: none; }
	.styled-select { width: 100%; padding: 10px 30px 10px 12px; border-radius: 8px; border: 1px solid #cbd5e1; background: #f8fafc; font-weight: 600; font-size: 0.9rem; color: #1e293b; appearance: none; cursor: pointer; transition: all 0.2s ease; }
	.styled-select:focus { border-color: #6366f1; background: #fff; box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1); outline: none; }

	.grid-inputs { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
	.input-box { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 6px 10px; transition: 0.2s; }
	.input-box:focus-within { border-color: #6366f1; background: white; }
	.input-box span { font-size: 0.65rem; font-weight: 700; color: #94a3b8; }
	.input-box input { border: none; background: transparent; font-weight: 600; width: 100%; outline: none; font-size: 0.9rem; }
	.input-box.wide { grid-column: span 2; }

	.primary-btn { width: 100%; background: #6366f1; color: white; border: none; padding: 14px; border-radius: 10px; font-weight: 700; cursor: pointer; margin-top: 10px; transition: 0.2s; }
	.primary-btn:hover { background: #4f46e5; transform: translateY(-1px); }
	
	.content { flex: 1; padding: 1rem; display: flex; flex-direction: column; gap: 0.75rem; align-items: center; justify-content: center; }
	.viz-card { background: white; padding: 1.5rem; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; }
	.viz-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem; }
	.viz-header h3 { margin: 0; font-size: 0.9rem; color: #475569; }
	.legend { display: flex; gap: 12px; font-size: 0.75rem; font-weight: 600; color: #64748b; }
	.l-item { display: flex; align-items: center; gap: 4px; }
	.l-item i { width: 10px; height: 10px; border-radius: 2px; display: block; }

	.msg-card { width: 100%; max-width: 600px; padding: 1rem 1.25rem; border-radius: 12px; display: flex; gap: 0.75rem; box-shadow: 0 2px 10px rgba(0,0,0,0.02); }
	.msg-card.success { background: #f0fdf4; border: 1px solid #bbf7d0; flex-direction: column; color: #166534; }
	.msg-card.error { background: #fff1f2; border: 1px solid #fecdd3; color: #9f1239; align-items: center; }
	.msg-icon { background: #fb7185; color: white; min-width: 24px; height: 24px; border-radius: 50%; display: grid; place-items: center; font-weight: 800; font-size: 0.8rem; }
	.msg-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
	.iter-tag { background: rgba(0,0,0,0.06); padding: 4px 10px; border-radius: 20px; font-size: 0.75rem; font-weight: 700; }
	
	.res-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; border-top: 1px solid rgba(0,0,0,0.05); padding-top: 0.75rem; }
	.res-item { display: flex; flex-direction: column; }
	.res-item span { font-size: 0.65rem; text-transform: uppercase; font-weight: 700; opacity: 0.7; }
	.res-item strong { font-size: 1rem; font-family: monospace; word-break: break-all; margin-top: 4px; }

	.spinner { width: 14px; height: 14px; border: 2px solid rgba(255,255,255,0.3); border-top-color: white; border-radius: 50%; animation: spin 0.8s linear infinite; }
	@keyframes spin { to { transform: rotate(360deg); } }
</style>