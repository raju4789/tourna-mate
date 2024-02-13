/* eslint-disable import/no-extraneous-dependencies */
/// <reference types="vitest" />
/// <reference types="vite/client" />

import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

const VITE_API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';
const VITE_UI_PORT = import.meta.env.VITE_UI_PORT || 8001;

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: VITE_UI_PORT,
    proxy: {
      '/api/v1': {
        target: VITE_API_URL, // The backend server
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