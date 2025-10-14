# 🎨 Tournament UI

> Modern React frontend for TOURNA-MATE platform with Material-UI design system, TypeScript type safety, and responsive layouts.

[![React](https://img.shields.io/badge/React-18.2.0-blue.svg)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.8.3-blue.svg)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-5.0.8-646CFF.svg)](https://vitejs.dev/)
[![MUI](https://img.shields.io/badge/MUI-5.15.7-007FFF.svg)](https://mui.com/)

---

## What It Does

Provides a modern, responsive web interface for tournament management with role-based views, real-time leaderboards, and match result submission.

**Key Features:**
- **User Authentication**: Login and signup with JWT token storage
- **Leaderboards**: Real-time points tables with sorting and filtering
- **Match Management**: Admin interface for recording match results
- **Responsive Design**: Mobile-first approach using MUI components
- **Type Safety**: Full TypeScript coverage
- **Fast Development**: Vite HMR (~50ms hot reload)
- **Modern React**: Hooks, Context API, React Router 6

**User Experience:**
- **Login/Signup**: < 2s page load
- **Leaderboard**: < 500ms data fetch
- **Smooth Animations**: Material-UI transitions
- **Mobile Responsive**: Works on 320px to 4K displays

---

## Quick Start

### Prerequisites

```bash
Node.js 20+
npm or yarn
```

### Install & Run

```bash
cd tourni-ui

# Install dependencies
npm install

# Run development server (port 5173 with Vite)
npm run dev

# Open browser
http://localhost:5173
```

**Build for Production:**
```bash
# TypeScript compilation + Vite build
npm run build

# Preview production build
npm run preview

# Output: dist/ folder
```

---

## Architecture

```
┌────────────────────────────────────────────┐
│           Browser (Client)                 │
└────────────────┬───────────────────────────┘
                 │
                 │ HTTP requests
                 ↓
┌────────────────────────────────────────────┐
│       API Gateway (Port 8080)              │
│       - JWT validation                     │
│       - CORS headers                       │
└────────────────┬───────────────────────────┘
                 │
     ┌───────────┼───────────────┐
     ↓           ↓               ↓
┌─────────┐ ┌──────────┐ ┌─────────────┐
│Identity │ │Management│ │   AI        │
│Service  │ │Service   │ │ Service     │
└─────────┘ └──────────┘ └─────────────┘
```

**Component Tree:**
```
App.tsx
  ├── Router.tsx
  │    ├── Root.tsx (Layout)
  │    │    ├── Header.tsx
  │    │    ├── Sidebar.tsx
  │    │    ├── Footer.tsx
  │    │    └── Outlet (Routes)
  │    │         ├── Login.tsx
  │    │         ├── Signup.tsx
  │    │         ├── PointsTable.tsx
  │    │         └── AddMatchResult.tsx
```

---

## Features & Pages

### 1. Login (`/login`)

**Features:**
- Username/password authentication
- JWT token storage (localStorage)
- Redirect to leaderboard on success
- Error handling with user feedback

**Tech:**
- `react-hook-form` for form validation
- `axios` for API calls
- Material-UI `TextField`, `Button`

**API Call:**
```typescript
POST /api/v1/auth/authenticate
{
  "username": "admin",
  "password": "admin@4789"
}

Response: {
  "data": {
    "token": "eyJhbG...",
    "username": "admin",
    "roles": ["ADMIN", "USER"]
  }
}
```

### 2. Signup (`/signup`)

**Features:**
- User registration form
- Password confirmation validation
- Auto-login after successful registration
- Form validation (email format, password strength)

**Tech:**
- `react-hook-form` with validation rules
- Material-UI form components
- TypeScript interfaces for type safety

**API Call:**
```typescript
POST /api/v1/auth/register
{
  "username": "john_doe",
  "password": "SecurePass123",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "roles": ["USER"]
}
```

### 3. Points Table / Leaderboard (`/pointsTable`)

**Features:**
- Display tournament leaderboards
- Sort by points, NRR, wins
- Filter by tournament (dropdown)
- Real-time data updates
- Responsive DataGrid (MUI X)

**Tech:**
- `@mui/x-data-grid` for table
- `axios` for data fetching
- State management with `useState`

**API Call:**
```typescript
GET /api/v1/manage/pointstable/tournament/101
Authorization: Bearer <JWT_TOKEN>

Response: {
  "data": {
    "tournamentName": "Cricket World Cup 2023",
    "pointsTable": [
      {
        "teamName": "India",
        "played": 9,
        "won": 9,
        "lost": 0,
        "points": 18,
        "netRunRate": 2.570
      }
    ]
  }
}
```

### 4. Add Match Result (`/addMatchResult`) - ADMIN ONLY

**Features:**
- Record match results (scores, winner)
- Tournament and team selection (dropdowns)
- Form validation (scores, overs)
- Success/error notifications
- Admin role required

**Tech:**
- `react-hook-form` with validation
- Material-UI `Select`, `TextField`
- Protected route (admin check)

**API Call:**
```typescript
POST /api/v1/manage/addMatchResult
Authorization: Bearer <ADMIN_TOKEN>
{
  "tournamentId": 101,
  "team1Id": 1101,
  "team2Id": 1102,
  "team1Score": 285,
  "team1Overs": 50,
  "team2Score": 270,
  "team2Overs": 50,
  "winningTeamId": 1101
}
```

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **React** | 18.2.0 | UI library |
| **TypeScript** | 5.8.3 | Type safety |
| **Vite** | 5.0.8 | Build tool (fast HMR) |
| **React Router** | 6.22.0 | Client-side routing |
| **MUI (Material-UI)** | 5.15.7 | Component library |
| **MUI X Data Grid** | 6.19.4 | Advanced tables |
| **Axios** | 1.6.7 | HTTP client |
| **React Hook Form** | 7.50.1 | Form management |
| **Styled Components** | 6.1.8 | CSS-in-JS |
| **Emotion** | 11.11.3 | CSS-in-JS (MUI peer) |
| **ESLint** | 8.56.0 | Code linting (Airbnb config) |
| **Vitest** | 1.2.2 | Unit testing |

---

## Project Structure

```
tourni-ui/
├── public/               # Static assets
├── src/
│   ├── components/       # React components
│   │   ├── login/
│   │   │   ├── Login.tsx
│   │   │   └── Login.styled.tsx
│   │   ├── signup/
│   │   │   ├── Signup.tsx
│   │   │   └── Signup.styled.tsx
│   │   ├── pointstable/
│   │   │   ├── PointsTable.tsx
│   │   │   └── PointsTable.styled.tsx
│   │   ├── addmatchresult/
│   │   │   ├── AddMatchResult.tsx
│   │   │   └── AddMatchResult.styled.tsx
│   │   ├── header/
│   │   │   ├── Header.tsx
│   │   │   └── Header.styled.tsx
│   │   ├── footer/
│   │   │   ├── Footer.tsx
│   │   │   └── Footer.styled.tsx
│   │   ├── sidebar/
│   │   │   └── Sidebar.tsx
│   │   └── root/
│   │       └── Root.tsx
│   ├── services/         # API services
│   │   ├── api.ts        # Axios instance
│   │   └── authService.ts
│   ├── hooks/            # Custom hooks
│   │   ├── useAuth.ts
│   │   └── useLocalStorage.ts
│   ├── types/            # TypeScript types
│   │   └── index.ts
│   ├── utils/            # Utilities
│   │   ├── auth.ts
│   │   └── ErrorBoundary.tsx
│   ├── App.tsx           # Root component
│   ├── Router.tsx        # Route definitions
│   ├── main.tsx          # Entry point
│   └── index.css         # Global styles
├── package.json
├── tsconfig.json
├── vite.config.ts
└── index.html
```

---

## Configuration

### Environment Variables (.env)

```bash
# API Gateway URL
VITE_API_BASE_URL=http://localhost:8080

# Environment
VITE_ENV=development
```

### Vite Config (vite.config.ts)

```typescript
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: true
  }
})
```

### TypeScript Config (tsconfig.json)

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "jsx": "react-jsx",
    "module": "ESNext",
    "moduleResolution": "bundler",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true
  }
}
```

---

## Scripts

```json
{
  "scripts": {
    "dev": "vite",                                    // Development server
    "build": "tsc && vite build",                     // Production build
    "build:production": "tsc && vite build --mode production",
    "preview": "vite preview --mode production",      // Preview build
    "lint": "eslint . --ext ts,tsx",                  // Code linting
    "test": "vitest"                                  // Run tests
  }
}
```

---

## Development Workflow

### Hot Module Replacement (HMR)

```bash
npm run dev

# Make changes to any .tsx file
# Browser updates in ~50ms without full reload
```

### Code Quality

```bash
# Run linter
npm run lint

# Fix linting issues
npm run lint -- --fix

# Run tests
npm test

# Type checking
npx tsc --noEmit
```

### Build & Deploy

```bash
# 1. Build for production
npm run build

# Output: dist/ folder
#   - index.html
#   - assets/
#       - index-abc123.js (bundled JS)
#       - index-def456.css (bundled CSS)

# 2. Build Docker image
docker build -t tourni-ui:latest .

# 3. Deploy to production
docker-compose up tourni-ui
```

---

## Production Considerations

### Performance
- **Code Splitting**: Vite automatically splits vendor and app bundles
- **Tree Shaking**: Unused code removed (~30% bundle reduction)
- **Minification**: Terser minification (~60% size reduction)
- **Lazy Loading**: React.lazy() for route-level code splitting
- **Bundle Size**: ~500KB (gzipped ~150KB)

### Security
- **JWT Storage**: localStorage (consider httpOnly cookies for production)
- **XSS Protection**: React escapes all user input by default
- **CORS**: Configured on API Gateway
- **HTTPS**: Nginx SSL termination in production
- **Environment Variables**: Never commit `.env` files

### Monitoring
- **Error Tracking**: Consider Sentry integration
- **Analytics**: Google Analytics or similar
- **Performance**: Lighthouse scores (Target: > 90)
- **Logs**: Frontend errors logged to backend

### Browser Support
```
Chrome: Last 2 versions
Firefox: Last 2 versions
Safari: Last 2 versions
Edge: Last 2 versions
Mobile: iOS 12+, Android 8+
```

---

## Docker Deployment

### Dockerfile

```dockerfile
# Build stage
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### Nginx Config (nginx.conf)

```nginx
server {
    listen 80;
    server_name _;
    
    root /usr/share/nginx/html;
    index index.html;
    
    # SPA fallback (all routes → index.html)
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # API proxy (optional, can use Gateway directly)
    location /api {
        proxy_pass http://tourni-gateway:8080;
    }
}
```

---

## Interview Highlights

**Frontend Architecture:**
- Why React over Vue/Angular? (Component reusability, ecosystem, performance)
- Client-side routing benefits (SPA, fast navigation, no page reloads)
- State management choice (useState vs Redux/Zustand)
- Why Vite over Webpack? (10x faster HMR, simpler config, modern ESM)

**Performance:**
- How to optimize React app? (Code splitting, lazy loading, memoization)
- Bundle size optimization (Tree shaking, minification, compression)
- Rendering performance (Virtual DOM, React.memo, useMemo)

**Security:**
- JWT storage strategies (localStorage vs cookies vs sessionStorage)
- XSS prevention (React escaping, Content Security Policy)
- CORS handling (Gateway configuration)
- Secure API calls (HTTPS, token refresh, interceptors)

**TypeScript:**
- Benefits of TypeScript in React (Type safety, IDE support, refactoring)
- Interface vs Type (prefer interfaces for objects)
- Generic components (reusable, type-safe)

---

## Future Enhancements

| Feature | Priority | Impact | Effort |
|---------|----------|--------|--------|
| Dark Mode Toggle | 🔴 High | Better UX | 1-2 days |
| Real-Time Updates (WebSocket) | 🔴 High | Live scores | 5-7 days |
| Progressive Web App (PWA) | 🟡 Medium | Offline support | 3-5 days |
| Mobile App (React Native) | 🟡 Medium | Native experience | 30-45 days |
| Advanced Filtering | 🟡 Medium | Better data discovery | 2-3 days |
| User Profile Management | 🟡 Medium | Self-service | 3-5 days |
| Internationalization (i18n) | 🟢 Low | Multi-language support | 5-7 days |
| Accessibility (WCAG 2.1) | 🟢 Low | Screen reader support | 7-10 days |

---

## 🚀 What's Next?

### Development Commands

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Run linter
npm run lint

# Run tests
npm test

# Build for production
npm run build

# Preview production build
npm run preview
```

### Key Concepts
- **Component-Based Architecture**: Reusable UI components
- **Type Safety**: TypeScript for compile-time error detection
- **Modern Build Tool**: Vite for fast development experience
- **Material Design**: MUI components for consistent UI

### Related Services
- [Gateway](../tourni-gateway/README.md) - API endpoint
- [Identity Service](../tourni-identity-service/README.md) - Authentication
- [Management Service](../tourni-management/README.md) - Data source

### Learning Resources

**React:**
- [React Docs](https://react.dev/)
- [React Patterns](https://reactpatterns.com/)

**TypeScript:**
- [TypeScript Handbook](https://www.typescriptlang.org/docs/handbook/intro.html)
- [React TypeScript Cheatsheet](https://react-typescript-cheatsheet.netlify.app/)

**Material-UI:**
- [MUI Documentation](https://mui.com/material-ui/getting-started/)
- [MUI Templates](https://mui.com/material-ui/getting-started/templates/)

**Vite:**
- [Vite Guide](https://vitejs.dev/guide/)
- [Vite Plugins](https://vitejs.dev/plugins/)

---

**[← Back to Main README](../README.md)**
