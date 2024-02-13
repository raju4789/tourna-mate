#!/bin/sh

# Echo environment variables to check if they are set correctly
echo "API_BASE_URL: $VITE_REACT_APP_API_URL"
echo "UI_PORT: $VITE_REACT_APP_UI_PORT"

# Start the Vite development server
exec npm run dev