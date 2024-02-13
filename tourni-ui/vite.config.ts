/* eslint-disable import/no-extraneous-dependencies */
/// <reference types="vitest" />
/// <reference types="vite/client" />

import { ConfigEnv, defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default ({ mode }: ConfigEnv) => {
  // Load the environment variables based on the mode
  process.env = { ...process.env, ...loadEnv(mode, process.cwd()) };
  // Access environment variables using process.env
  const VITE_API_URL = process.env.VITE_API_URL || 'http://localhost:8080';
  const VITE_UI_PORT = process.env.VITE_UI_PORT ? parseInt(process.env.VITE_UI_PORT, 10) : 8001;

  console.log('VITE_API_URL:', VITE_API_URL);
  console.log('VITE_UI_PORT:', VITE_UI_PORT);

  return defineConfig({
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
};
