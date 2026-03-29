// svelte.config.js
import adapter from '@sveltejs/adapter-node'; // <-- Импортируем node-адаптер

export default {
  kit: {
    adapter: adapter({
      // Опции (необязательно)
      out: 'build' // Это папка по умолчанию
    }),
	paths: {
      base: '/lab3'
    }
  }
};