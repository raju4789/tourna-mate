/* eslint-disable import/no-extraneous-dependencies */
/// <reference types="vitest" />
/// <reference types="vite/client" />

import { ConfigEnv, defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default ({ mode }: ConfigEnv) => {
  const VITE_API_URL: string = (mode !== 'production') ? 'http://localhost:8080' : 'https://tourni-gateway:8080';
  const VITE_UI_PORT: number = (mode !== 'production') ? 8001 : 8000;

  console.log('VITE_API_URL:', VITE_API_URL);
  console.log('VITE_UI_PORT:', VITE_UI_PORT);

  return defineConfig({
    plugins: [react()],
    preview: {
      host: true,
      port: VITE_UI_PORT,
    },
    server: {
      port: VITE_UI_PORT,
      host: true,
      strictPort: true,
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
