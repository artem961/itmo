<script lang="ts">
	import { onMount } from 'svelte';
	import functionPlot from 'function-plot';
	import { slide } from 'svelte/transition';

	interface Point { x: number; y: number; }
	interface TableRow {
		x: number | null;
		y: number | null;
		yExact: number | null;
		predicted: number | null;
		delta: number | null;
		integral: number | null;
		exactIntegral: number | null;
	}
	interface MethodSeries {
		id: number;
		label: string;
		points: Point[];
		localErrors: Array<number | null>;
		table: TableRow[];
		rungeError: number | null;
		maxExactError: number | null;
	}

	function toNumber(value: unknown): number | null {
		return typeof value === 'number' && Number.isFinite(value) ? value : null;
	}

	function normalizePoint(value: any): Point | null {
		const x = toNumber(value?.x);
		const y = toNumber(value?.y);
		return x === null || y === null ? null : { x, y };
	}

	function normalizeTableRow(value: any): TableRow {
		return {
			x: toNumber(value?.x),
			y: toNumber(value?.y),
			yExact: toNumber(value?.yExact),
			predicted: null,
			delta: toNumber(value?.delta),
			integral: null,
			exactIntegral: null
		};
	}

	function getLastPoint(points: Point[]) {
		return points.length > 0 ? points[points.length - 1] : null;
	}

	function formatNumber(value: number | null | undefined, fractionDigits = 5) {
		return typeof value === 'number' && Number.isFinite(value) ? value.toFixed(fractionDigits) : '—';
	}

	function formatPlain(value: number | null | undefined, fractionDigits = 6) {
		if (typeof value === 'number' && Number.isFinite(value)) {
			// use fixed notation and trim trailing zeros
			const s = value.toFixed(fractionDigits);
			// remove trailing zeros and possible trailing dot
			return s.indexOf('.') >= 0 ? s.replace(/\.0+$/, '').replace(/(\.\d*?)0+$/, '$1') : s;
		}
		return '—';
	}

	function normalizeSeries(series: any, idx: number, exactCount: number): MethodSeries {
		const points = Array.isArray(series?.points)
			? series.points.map((point: any) => normalizePoint(point)).filter((point: Point | null): point is Point => point !== null)
			: [];
		const localErrors = Array.isArray(series?.localErrors)
			? series.localErrors.map((value: any) => toNumber(value))
			: [];
		const table = Array.isArray(series?.table)
			? series.table.map(normalizeTableRow)
			: [];

		if (table.length > 0) {
			return {
				id: idx,
				label: series?.methodName || `Метод ${idx + 1}`,
				points,
				localErrors,
				table,
				rungeError: toNumber(series?.rungeError),
				maxExactError: toNumber(series?.maxExactError)
			};
		}

		const fallbackRows = Array.from({ length: Math.max(points.length, localErrors.length, exactCount) }, (_, rowIndex) => ({
			x: points[rowIndex]?.x ?? null,
			y: points[rowIndex]?.y ?? null,
			yExact: exactData[rowIndex]?.y ?? null,
			predicted: null,
			delta: localErrors[rowIndex] ?? null,
			integral: null,
			exactIntegral: null
		}));

		return {
			id: idx,
			label: series?.methodName || `Метод ${idx + 1}`,
			points,
			localErrors,
			table: fallbackRows,
			rungeError: toNumber(series?.rungeError),
			maxExactError: toNumber(series?.maxExactError)
		};
	}

	const ODE_API_BASE = 'https://comp-math.arhr.tech/lab6/api/ode';
	const COLORS = ['#2563eb', '#dc2626', '#d97706', '#16a34a', '#9333ea', '#0891b2'];

	// State
	let equations: Record<number,string> = $state({});
	let targetEquationId = $state<number | null>(1);
	let targetX0 = $state<number>(0);
	let targetY0 = $state<number>(1);
	let targetXn = $state<number>(2);
	let targetH = $state<number>(0.1);
	let targetEps = $state<number>(0.000001);

	let exactData = $state<Point[]>([]);
	let seriesData = $state<MethodSeries[]>([]);
	let visibleMethods = $state<Set<number>>(new Set());

	let isLoading = $state(false);
	let errorMsg = $state<string | null>(null);

	let plotContainer: HTMLDivElement | undefined = $state();
	let solutionInstance: any = null;
	let shouldResetSolutionScale = true;

	onMount(async () => {
		try {
			const res = await fetch(`${ODE_API_BASE}/equations`);
			if (res.ok) {
				const data = await res.json();
				if (data) {
					// reapply equations safely
					for (const [key, value] of Object.entries(data as Record<string, string>)) {
						equations[Number(key)] = value;
					}
				}
				if (Object.keys(equations).length > 0) {
					targetEquationId = Number(Object.keys(equations)[0]);
				}
			}
		} catch (e) {
			// fallback mapping if backend is down during UI dev
			equations = { 1: "y' = y", 2: "y' = x + y", 3: "y' = -y + x" };
		}
	});

	$effect(() => {
		if (plotContainer) drawSolutionPlot();
	});

	function renderFunctionPlot(container: HTMLDivElement | undefined, previousInstance: any, data: any[], resetScale: boolean) {
		if (!container) return previousInstance;
		if (data.length === 0) {
			container.innerHTML = '';
			return previousInstance;
		}
		
		let xDomain = [-2, 12];
		let yDomain = [-2, 12];
		
		if (!resetScale && previousInstance?.meta?.xScale && previousInstance?.meta?.yScale) {
			xDomain = previousInstance.meta.xScale.domain();
			yDomain = previousInstance.meta.yScale.domain();
		}
		container.innerHTML = '';

		return functionPlot({
			target: container,
			width: container.clientWidth,
			height: 520,
			grid: true,
			data: data,
			xAxis: { domain: xDomain },
			yAxis: { domain: yDomain }
		});
	}

	function drawSolutionPlot() {
		const data: any[] = [];

		seriesData.forEach((res) => {
			if (visibleMethods.has(res.id) && res.points.length > 0) {
				data.push({
					points: res.points.map((p) => [p.x, p.y]),
					fnType: 'points',
					graphType: 'polyline',
					color: COLORS[res.id % COLORS.length],
					attr: { fill: 'transparent', 'stroke-width': 2.5 }
				});
			}
		});

		if (exactData.length > 0) {
			data.push({
				points: exactData.map((p) => [p.x, p.y]),
				fnType: 'points',
				graphType: 'polyline',
				color: '#0f172a',
				attr: { 'stroke-width': 3 }
			});
		}

		solutionInstance = renderFunctionPlot(plotContainer, solutionInstance, data, shouldResetSolutionScale);
		shouldResetSolutionScale = false;
	}

	function getDisplayRows(res: MethodSeries) {
		if (res.table.length > 0) {
			return res.table;
		}

		const rowCount = Math.max(res.points.length, res.localErrors.length, exactData.length);

		return Array.from({ length: rowCount }, (_, rowIndex) => ({
			x: res.points[rowIndex]?.x ?? exactData[rowIndex]?.x ?? null,
			y: res.points[rowIndex]?.y ?? null,
			yExact: exactData[rowIndex]?.y ?? null,
			predicted: null,
			delta: res.localErrors[rowIndex] ?? null,
			integral: null,
			exactIntegral: null
		}));
	}

	async function handleSubmit() {
		if (!targetEquationId) { errorMsg = 'Выберите уравнение'; return; }
		if (targetH <= 0) { errorMsg = 'Шаг h должен быть > 0'; return; }
		if (targetEps <= 0) { errorMsg = 'eps должен быть > 0'; return; }
		if (targetXn <= targetX0) { errorMsg = 'xn должен быть > x0'; return; }

		isLoading = true; errorMsg = null;
		try {
			const body = {
				equationId: targetEquationId,
				x0: targetX0,
				y0: targetY0,
				xn: targetXn,
				h: targetH,
				eps: targetEps
			};

			const res = await fetch(`${ODE_API_BASE}/solve`, {
				method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body)
			});
			const data = await res.json();
			if (data.error || data.message) {
				errorMsg = data.error || data.message;
			} else {
					exactData = Array.isArray(data.exactPoints)
						? data.exactPoints.map((point: any) => normalizePoint(point)).filter((point: Point | null): point is Point => point !== null)
					: [];
				seriesData = Array.isArray(data.series)
					? data.series.map((s: any, idx: number) => normalizeSeries(s, idx, exactData.length))
					: [];
				
				visibleMethods = new Set(seriesData.map(s => s.id));
				shouldResetSolutionScale = true;
			}
		} catch (e) {
			errorMsg = "Ошибка соединения с сервером";
		} finally {
			isLoading = false;
		}
	}
</script>

<div class="app">
	<header class="header">
		<div class="header-left">
			<div class="logo">ODE</div>
			<div class="header-title">
				<span class="title-main">CompMath</span>
				<span class="title-sub">Лабораторная работа №6: ОДУ</span>
			</div>
		</div>
		<span class="author">Храбров Артём</span>
	</header>

	<div class="layout">
		<aside class="sidebar">
			<div class="func-panel">
				<label class="field-label" for="equation-select">Уравнение</label>
				<select class="func-select" id="equation-select" bind:value={targetEquationId}>
					{#each Object.entries(equations) as [key, description]}
						<option value={+key}>{description}</option>
					{/each}
				</select>
				
				<div class="func-params">
					<div class="param-group">
						<label for="param-x0">x0</label>
						<input id="param-x0" type="number" step="0.01" bind:value={targetX0} />
					</div>
					<div class="param-group">
						<label for="param-y0">y0</label>
						<input id="param-y0" type="number" step="0.01" bind:value={targetY0} />
					</div>
					<div class="param-group">
						<label for="param-xn">xn</label>
						<input id="param-xn" type="number" step="0.01" bind:value={targetXn} />
					</div>
					<div class="param-group">
						<label for="param-h">h</label>
						<input id="param-h" type="number" step="0.001" bind:value={targetH} />
					</div>
					<div class="param-group">
						<label for="param-eps">eps</label>
						<input id="param-eps" type="number" step="0.000001" bind:value={targetEps} />
					</div>
				</div>
			</div>

			<button
				class="btn-run"
				onclick={handleSubmit}
				disabled={isLoading}
			>
				{#if isLoading}<span class="spinner"></span>{/if}
				Вычислить
			</button>
		</aside>

		<main class="chart-area">
			<div class="chart-card">
				<div class="chart-meta">
					<span class="chart-label">График решения</span>
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
			</div>

			<div class="results-list">
				{#if seriesData.length === 0}
					<div class="results-empty">
						<p>Задайте параметры и нажмите «Вычислить»</p>
					</div>
				{:else}
					<!-- Точное решение -->
					{#if exactData && exactData.length > 0}
						{@const lastExact = getLastPoint(exactData)}
						<div class="result-card" style="--c: #0f172a; border-left: 3px solid #0f172a;">
							<div class="rc-head">
								<div class="rc-title">
									<span class="rc-dot" style="background:#0f172a"></span>
									<strong>Точное решение</strong>
								</div>
							</div>
							{#if lastExact}
								<div class="rc-value">
									<span class="rc-eq">y({lastExact.x.toFixed(3)})</span>
									<span class="rc-num" style="color:#0f172a">{lastExact.y.toFixed(5)}</span>
								</div>
							{/if}
							<!-- exact integral removed per simplified spec -->
						</div>
					{/if}

					<!-- Методы -->
					{#each seriesData as res}
						{@const color = COLORS[res.id % COLORS.length]}
						{@const visible = visibleMethods.has(res.id)}
						{@const failed = res.points.length === 0}
							{@const last = getLastPoint(res.points)}
						<div
							class="result-card"
							class:is-muted={!visible}
							class:is-failed={failed}
							style="--c: {color}"
						>
							<div class="rc-head">
								<div class="rc-title">
									<span class="rc-dot" style="background:{color}"></span>
									<strong>{res.label}</strong>
								</div>
								<label class="rc-toggle" title="Показать / скрыть">
									<input
										type="checkbox"
										checked={visible}
										onclick={() => {
											if (visibleMethods.has(res.id)) visibleMethods.delete(res.id);
											else visibleMethods.add(res.id);
											visibleMethods = new Set(visibleMethods);
										}}
									/>
									<span class="toggle-track"><span class="toggle-thumb"></span></span>
								</label>
							</div>

							{#if !failed}
								{#if last}
									<div class="rc-value">
										<span class="rc-eq">y({last.x.toFixed(3)})</span>
										<span class="rc-num" style="color:{color}">{last.y.toFixed(5)}</span>
									</div>
								{/if}
							<!-- predicted and integral displays removed per simplified spec -->
								{#if res.maxExactError !== null && /adam/i.test(res.label)}
									<div class="rc-msg">Макс. ошибка: {formatPlain(res.maxExactError, 6)}</div>
								{/if}
								{#if res.rungeError !== null}
									<div class="rc-msg">Оценка Рунге: {formatPlain(res.rungeError, 6)}</div>
								{/if}
								{#if getDisplayRows(res).length > 0}
									<details class="rc-details">
										<summary>Таблица по узлам</summary>
										<div class="table-wrap">
											<table>
											<thead>
												<tr>
													<th>x</th>
													<th>y (прибл.)</th>
													<th>y (точн.)</th>
													<th>Погрешность</th>
												</tr>
											</thead>
												<tbody>
													{#each getDisplayRows(res) as row}
														<tr>
															<td>{formatNumber(row.x, 4)}</td>
															<td>{formatNumber(row.y, 6)}</td>
															<td>{formatNumber(row.yExact, 6)}</td>
															<td>{formatPlain(row.delta, 6)}</td>
														</tr>
													{/each}
												</tbody>
											</table>
										</div>
									</details>
								{/if}
							{:else}
								<div class="rc-na">Отсутствуют точки</div>
							{/if}
						</div>
					{/each}
				{/if}
			</div>
		</aside>
	</div>
</div>

<style>
	@import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Mono:wght@400;500&family=IBM+Plex+Sans:wght@400;500;600;700&display=swap');

	:global(*) { box-sizing: border-box; }
	:global(body) {
			margin: 0;
			font-family: 'IBM Plex Sans', system-ui, sans-serif;
			background: #f8f9fb;
			color: #111827;
			overflow: auto;
			-webkit-font-smoothing: antialiased;
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
		font-size: 0.75rem; font-weight: 700;
		font-family: 'IBM Plex Mono', monospace;
	}
	.title-main { display: block; font-size: 0.875rem; font-weight: 700; line-height: 1.1; color: #111827; }
	.title-sub { display: block; font-size: 0.625rem; color: #9ca3af; font-weight: 400; }
	.author { font-size: 0.75rem; color: #9ca3af; font-weight: 500; }

	.layout {
		display: grid;
		grid-template-columns: 256px 1fr 480px;
		flex: 1;
		overflow: hidden;
	}

	.sidebar {
		background: #fff;
		border-right: 1px solid #e5e7eb;
		display: flex;
		flex-direction: column;
		padding: 14px 12px;
		gap: 14px;
		overflow-y: auto;
	}

	.func-panel {
		background: #f9fafb;
		border: 1px solid #e5e7eb;
		border-radius: 8px;
		padding: 12px;
		display: flex;
		flex-direction: column;
		gap: 12px;
	}
	.field-label {
		display: block;
		font-size: 0.65rem;
		font-weight: 700;
		text-transform: uppercase;
		letter-spacing: 0.08em;
		color: #9ca3af;
	}
	.func-select {
		width: 100%;
		background: #fff;
		border: 1px solid #e5e7eb;
		border-radius: 6px;
		padding: 8px;
		font-family: 'IBM Plex Mono', monospace;
		font-size: 0.8rem;
		color: #111827;
		outline: none;
		cursor: pointer;
	}
	.func-params { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; }
	.param-group { display: flex; flex-direction: column; gap: 4px; }
	.param-group label {
		font-size: 0.65rem;
		font-weight: 700;
		text-transform: uppercase;
		color: #6b7280;
	}
	.param-group input {
		width: 100%;
		background: #fff;
		border: 1px solid #e5e7eb;
		border-radius: 6px;
		padding: 6px 8px;
		font-family: 'IBM Plex Mono', monospace;
		font-size: 0.8rem;
		color: #111827;
		outline: none;
	}
	.param-group input:focus { border-color: #6b7280; }

	.btn-run {
		width: 100%;
		background: #111827;
		color: #fff;
		border: none;
		border-radius: 8px;
		padding: 12px;
		font-family: 'IBM Plex Sans', sans-serif;
		font-size: 0.85rem;
		font-weight: 600;
		cursor: pointer;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 8px;
		transition: all 0.15s;
	}
	.btn-run:hover:not(:disabled) { background: #1f2937; }
	.btn-run:disabled { background: #e5e7eb; color: #9ca3af; cursor: not-allowed; }
	.spinner {
		width: 14px; height: 14px;
		border: 2px solid rgba(255,255,255,0.25);
		border-top-color: #fff;
		border-radius: 50%;
		animation: spin 0.75s linear infinite;
	}
	@keyframes spin { to { transform: rotate(360deg); } }

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
		min-height: 0;
		background: #fff;
		border: 1px solid #e5e7eb;
		border-radius: 10px;
		display: flex;
		flex-direction: column;
		overflow: hidden;
	}
	.chart-meta {
		padding: 12px 16px;
		border-bottom: 1px solid #f3f4f6;
	}
	.chart-label {
		font-size: 0.65rem;
		font-weight: 700;
		text-transform: uppercase;
		letter-spacing: 0.08em;
		color: #9ca3af;
	}
	.plot-box {
		flex: 1;
		min-height: 0;
		overflow: hidden;
	}
	:global(.plot-box svg) { display: block; }

	.error-bar {
		background: #fef2f2;
		border: 1px solid #fecaca;
		border-radius: 8px;
		padding: 10px 16px;
		font-size: 0.8rem;
		font-weight: 500;
		color: #dc2626;
	}

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
		padding: 4px 6px;
	}
	.results-list {
		flex: 1;
		overflow-y: auto;
		display: flex;
		flex-direction: column;
		gap: 8px;
	}
	.results-empty {
		padding: 2rem;
		text-align: center;
		font-size: 0.8rem;
		color: #9ca3af;
	}

	.result-card {
		border: 1px solid #e5e7eb;
		border-left: 3px solid var(--c);
		border-radius: 8px;
		padding: 12px 14px;
		background: #fff;
		transition: opacity 0.18s, filter 0.18s;
	}
	.result-card.is-muted { opacity: 0.4; filter: grayscale(0.8); }
	.rc-head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 10px;
	}
	.rc-title { display: flex; align-items: center; gap: 8px; }
	.rc-dot { width: 10px; height: 10px; border-radius: 50%; }
	.rc-title strong { font-size: 0.75rem; font-weight: 600; color: #374151; }

	/* Toggle */
	.rc-toggle { cursor: pointer; }
	.rc-toggle input { display: none; }
	.toggle-track {
		display: block;
		width: 32px; height: 18px;
		background: #e5e7eb;
		border-radius: 10px;
		position: relative;
		transition: background 0.2s;
	}
	.rc-toggle input:checked ~ .toggle-track { background: var(--c); }
	.toggle-thumb {
		position: absolute;
		top: 2px; left: 2px;
		width: 14px; height: 14px;
		background: #fff;
		border-radius: 50%;
		box-shadow: 0 1px 2px rgba(0,0,0,0.15);
		transition: left 0.2s;
	}
	.rc-toggle input:checked ~ .toggle-track .toggle-thumb { left: 16px; }

	.rc-value {
		display: flex;
		align-items: baseline;
		gap: 8px;
		font-family: 'IBM Plex Mono', monospace;
	}

	.rc-eq { font-size: 0.75rem; color: #6b7280; }
	.rc-num { font-size: 1.1rem; font-weight: 500; }
	.rc-msg {
		margin-top: 8px;
		font-size: 0.75rem;
		color: #4b5563;
		background: #f9fafb;
		border: 1px solid #f3f4f6;
		border-radius: 6px;
		padding: 6px 10px;
		font-family: 'IBM Plex Mono', monospace;
	}
	.rc-details {
		margin-top: 10px;
	}
	.rc-details summary {
		cursor: pointer;
		font-size: 0.75rem;
		font-weight: 600;
		color: #374151;
	}
	.table-wrap {
		margin-top: 8px;
		overflow: auto;
		max-height: 220px;
		border: 1px solid #eef2f7;
		border-radius: 8px;
	}
	table {
		width: 100%;
		border-collapse: collapse;
		font-family: 'IBM Plex Mono', monospace;
		font-size: 0.72rem;
	}
	thead th {
		position: sticky;
		top: 0;
		background: #f9fafb;
		text-align: left;
		padding: 8px 10px;
		border-bottom: 1px solid #e5e7eb;
		color: #374151;
	}
	tbody td {
		padding: 7px 10px;
		border-bottom: 1px solid #f3f4f6;
		color: #111827;
		white-space: nowrap;
	}
	tbody tr:last-child td { border-bottom: none; }

@media (max-width: 900px) {
	.layout { display: flex; flex-direction: column; grid-template-columns: none; }
	.sidebar, .results-panel, .chart-area { width: 100%; padding: 10px; }
	.chart-area { order: 1; }
	.results-panel { order: 2; }
	.sidebar { order: 3; }
	.plot-box { height: 360px; }
	.table-wrap { max-height: 180px; }
	.func-params { grid-template-columns: 1fr; }
	.param-group input { width: 100%; }
	.rc-value { flex-direction: column; align-items: flex-start; gap: 6px; }
	.rc-num { font-size: 1rem; }
	.toggle-track { transform: scale(0.9); }
}
</style>
