// svelte.config.js
import adapter from '@sveltejs/adapter-node'; 

export default {
  kit: {
    adapter: adapter({
      out: 'build'
    }),
	paths: {
      base: '/lab4'
    }
  }
};