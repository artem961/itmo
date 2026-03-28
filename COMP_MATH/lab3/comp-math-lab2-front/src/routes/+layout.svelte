<script lang="ts">
	import duck from '$lib/assets/duck.png'


	let { children } = $props();
</script>

<svelte:head>
	<link rel="icon" href={duck} />
	<link rel="stylesheet" href="node_modules/svelte-material-ui/bare.css" />
	 <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/svelte-material-ui@7.0.0/bare.css" />
	 <title>Lab 2</title>

</svelte:head>

{@render children()}
