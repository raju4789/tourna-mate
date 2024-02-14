/* eslint-disable import/no-extraneous-dependencies */
/// <reference types="vitest" />
/// <reference types="vite/client" />

import { ConfigEnv, defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default ({ mode }: ConfigEnv) => {

  return defineConfig({
    plugins: [react()],
    preview: {
      host: true,
      port: 8000,
    },
    server: {
      port: 8000,
      host: true,
      strictPort: true,
      proxy: {
        '/api/v1': {
          target: 'https://tourni-gateway:8080', // The backend server
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
