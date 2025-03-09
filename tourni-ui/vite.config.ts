/* eslint-disable import/no-extraneous-dependencies */
/// <reference types="vitest" />
/// <reference types="vite/client" />

import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default () => defineConfig({
  plugins: [react()],
  server: {
    port: 8001,
    host: true,
    strictPort: true,
    proxy: {
      '/api/v1': {
        target: 'http://localhost:8080', // The backend server
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
