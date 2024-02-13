// inject-env.js
const fs = require('fs');
const path = require('path');

const envVariables = {
  API_BASE_URL: process.env.VITE_REACT_APP_API_URL,
  UI_PORT: process.env.VITE_REACT_APP_UI_PORT,
};

const content = `window.env = ${JSON.stringify(envVariables)};`;

fs.writeFileSync(path.join(__dirname, 'public', 'env.js'), content);