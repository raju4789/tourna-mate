/* eslint-disable import/no-extraneous-dependencies */
/// <reference types="vitest" />
/// <reference types="vite/client" />

import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

const API_BASE_URL = window.env.API_BASE_URL || 'http://localhost:8080';
const UI_PORT = window.env.UI_PORT || 8001;

console.log('API_BASE_URL: ', API_BASE_URL);
console.log('UI_PORT: ', UI_PORT);

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,
    port: UI_PORT,
    proxy: {
      '/api/v1': {
        target: API_BASE_URL, // The backend server
        changeOrigin: true, // Needed for virtual hosted sites
        secure: false, // Set to true if your backend is served over HTTPS
      },
    },
  },
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/setupTests.ts'],
  },
});
