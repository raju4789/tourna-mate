# Tournament UI

> React TypeScript frontend with Material-UI and Vite

[![React](https://img.shields.io/badge/React-18.2-blue.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.8-blue.svg)](https://www.typescriptlang.org/)
[![Material-UI](https://img.shields.io/badge/Material--UI-5.15-blue.svg)](https://mui.com/)
[![Vite](https://img.shields.io/badge/Vite-5.0-purple.svg)](https://vitejs.dev/)

---

## Purpose

Single-page application (SPA) providing user interface for tournament management. Built with modern React patterns including hooks, context API, and TypeScript for type safety.

### Core Features

- **Authentication**: JWT-based login with role-based UI rendering
- **Tournament Management**: View and manage tournaments
- **Match Results**: Record match scores (ADMIN only)
- **Live Leaderboard**: Real-time points table with Net Run Rate
- **Responsive Design**: Mobile-first Material-UI components
- **Type Safety**: TypeScript prevents runtime errors

---

## Architecture

```
┌────────────────────────────────────────────┐
│   Browser                                  │
│   ┌────────────────────────────────────┐  │
│   │   React Application                │  │
│   │                                    │  │
│   │   ┌──────────────────────────┐    │  │
│   │   │ Component Tree           │    │  │
│   │   │                          │    │  │
│   │   │ App                      │    │  │
│   │   │  ├─ Router              │    │  │
│   │   │  │  ├─ Login            │    │  │
│   │   │  │  ├─ Signup           │    │  │
│   │   │  │  ├─ PointsTable      │    │  │
│   │   │  │  └─ AddMatchResult   │    │  │
│   │   │  │                      │    │  │
│   │   │  └─ AuthContext         │    │  │
│   │   └──────────────────────────┘    │  │
│   │                                    │  │
│   │   State Management:                │  │
│   │   • AuthContext (user, token)     │  │
│   │   • Local component state         │  │
│   └────────────────┬───────────────────┘  │
│                    │                       │
│                    │ Axios HTTP Client     │
│                    │                       │
└────────────────────┼───────────────────────┘
                     │
                     │ REST API
                     ▼
        ┌────────────────────────┐
        │   API Gateway (8080)   │
        │   • Authentication     │
        │   • Authorization      │
        │   • Routing            │
        └────────────────────────┘
```

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **React** | 18.2 | UI library with concurrent rendering |
| **TypeScript** | 5.8 | Static typing, compile-time error detection |
| **Material-UI (MUI)** | 5.15 | Component library, professional design |
| **React Router** | 6.21 | Client-side routing |
| **Axios** | 1.6.5 | HTTP client with interceptors |
| **Vite** | 5.0 | Build tool (10-100x faster than Webpack) |

---

## Project Structure

```
tourni-ui/
├── src/
│   ├── main.tsx              # Application entry point
│   ├── App.tsx                # Root component
│   ├── Router.tsx             # Route definitions
│   │
│   ├── components/            # React components
│   │   ├── Login.tsx          # Login form
│   │   ├── Signup.tsx         # Registration form
│   │   ├── PointsTable.tsx    # Leaderboard display
│   │   ├── AddMatchResult.tsx # Match result form (ADMIN)
│   │   ├── Navbar.tsx         # Navigation bar
│   │   └── ...
│   │
│   ├── services/              # API clients
│   │   ├── api.ts             # Axios instance with interceptors
│   │   └── authService.ts     # Authentication APIs
│   │
│   ├── hooks/                 # Custom React hooks
│   │   ├── useAuth.tsx        # Authentication hook
│   │   └── useApi.ts          # API call hook
│   │
│   ├── types/                 # TypeScript interfaces
│   │   └── index.ts           # API response types
│   │
│   └── utils/                 # Utility functions
│       └── AuthContext.tsx    # Context for auth state
│
├── public/                    # Static assets
├── index.html                 # HTML template
├── vite.config.ts             # Vite configuration
├── tsconfig.json              # TypeScript configuration
└── package.json               # Dependencies
```

---

## Key Components

### 1. Login Component

**Path**: `src/components/Login.tsx`

**Responsibilities**:
- Username/password form
- Call authentication API
- Store JWT token in localStorage
- Redirect to dashboard on success

**Implementation**:
```typescript
const handleLogin = async () => {
  try {
    const response = await authService.login(username, password);
    
    // Store token
    localStorage.setItem('token', response.data.token);
    localStorage.setItem('username', response.data.username);
    localStorage.setItem('roles', JSON.stringify(response.data.roles));
    
    // Update auth context
    setUser(response.data);
    
    // Redirect
    navigate('/dashboard');
  } catch (error) {
    setError('Invalid credentials');
  }
};
```

### 2. Points Table Component

**Path**: `src/components/PointsTable.tsx`

**Responsibilities**:
- Fetch leaderboard from API
- Display in Material-UI Table
- Sort by points and NRR
- Refresh on demand

**Features**:
- Loading state
- Error handling
- Responsive table (horizontal scroll on mobile)
- Highlight current user's team (if applicable)

### 3. Add Match Result Component

**Path**: `src/components/AddMatchResult.tsx`

**Responsibilities**:
- Form to input match details
- Validate input (scores, teams)
- POST to API (ADMIN only)
- Show success/error messages

**Authorization**:
```typescript
// Only ADMIN can access
if (!user.roles.includes('ADMIN')) {
  return <Navigate to="/unauthorized" />;
}
```

---

## Authentication Flow

### Login

```
1. User enters username/password
2. Submit to POST /api/v1/auth/authenticate
3. Receive JWT token + user details
4. Store in localStorage:
   - token
   - username
   - roles (ADMIN, USER)
5. Update React Context
6. Redirect to dashboard
7. Subsequent requests: Axios interceptor adds Authorization header
```

### Axios Interceptor

```typescript
// Add token to all requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle 401 Unauthorized
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired, logout
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

### Protected Routes

```typescript
// Router.tsx
const ProtectedRoute = ({ children }) => {
  const { user } = useAuth();
  
  if (!user) {
    return <Navigate to="/login" />;
  }
  
  return children;
};

<Route path="/dashboard" element={
  <ProtectedRoute>
    <Dashboard />
  </ProtectedRoute>
} />
```

---

## State Management

### Authentication Context

**Path**: `src/utils/AuthContext.tsx`

**Purpose**: Share authentication state across components without prop drilling

**Implementation**:
```typescript
interface AuthContextType {
  user: User | null;
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  
  useEffect(() => {
    // Restore user from localStorage on mount
    const token = localStorage.getItem('token');
    if (token) {
      const username = localStorage.getItem('username');
      const roles = JSON.parse(localStorage.getItem('roles') || '[]');
      setUser({ username, roles, token });
    }
  }, []);
  
  const logout = () => {
    localStorage.clear();
    setUser(null);
  };
  
  return (
    <AuthContext.Provider value={{ user, login, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
};
```

**Usage**:
```typescript
const { user, isAuthenticated, logout } = useAuth();

if (user.roles.includes('ADMIN')) {
  // Show admin features
}
```

---

## API Integration

### API Service

**Path**: `src/services/api.ts`

```typescript
import axios from 'axios';

// Base URL from environment variable
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

### API Calls

```typescript
// Get points table
export const getPointsTable = (tournamentId: number) => {
  return api.get(`/api/v1/manage/pointstable/tournament/${tournamentId}`);
};

// Add match result (ADMIN only)
export const addMatchResult = (data: MatchResultRequest) => {
  return api.post('/api/v1/manage/addMatchResult', data);
};
```

---

## TypeScript Types

**Path**: `src/types/index.ts`

```typescript
export interface User {
  username: string;
  email: string;
  roles: string[];
  token: string;
}

export interface AuthResponse {
  status: string;
  data: {
    token: string;
    expiryTime: string;
    username: string;
    email: string;
    roles: string[];
  };
}

export interface PointsTableEntry {
  position: number;
  teamName: string;
  played: number;
  won: number;
  lost: number;
  points: number;
  netRunRate: number;
}

export interface MatchResultRequest {
  tournamentId: number;
  team1Id: number;
  team2Id: number;
  team1Score: number;
  team1Overs: number;
  team2Score: number;
  team2Overs: number;
  winningTeamId: number;
}
```

**Benefits**:
- Auto-completion in IDE
- Compile-time error detection
- Self-documenting code
- Refactoring safety

---

## Development

### Prerequisites

```bash
# Node.js 20+
node --version

# npm or yarn
npm --version
```

### Install Dependencies

```bash
cd tourni-ui
npm install
```

### Run Development Server

```bash
npm run dev

# Vite dev server starts on http://localhost:5173
# Hot Module Replacement (HMR) enabled
```

### Build for Production

```bash
npm run build

# Output: dist/
# Optimized, minified, tree-shaken bundle
```

### Preview Production Build

```bash
npm run preview

# Serves production build locally for testing
```

---

## Docker Deployment

### Dockerfile

**Multi-stage build**:

```dockerfile
# Stage 1: Build
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 8000
CMD ["nginx", "-g", "daemon off;"]
```

### Nginx Configuration

**Path**: `nginx.conf`

```nginx
server {
    listen 8000;
    server_name localhost;
    
    root /usr/share/nginx/html;
    index index.html;
    
    # SPA routing - all routes serve index.html
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # API proxy to backend
    location /api {
        proxy_pass http://tourni-gateway-dev:8080;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    
    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header X-Content-Type-Options "nosniff" always;
}
```

---

## Performance Optimization

### Vite Benefits

- **Fast HMR**: 10-100x faster than Webpack
- **Native ESM**: Leverages browser ES modules
- **Code Splitting**: Automatic chunk splitting
- **Tree Shaking**: Removes unused code
- **Asset Optimization**: Image compression, CSS minification

### Lazy Loading

```typescript
// Lazy load components
const PointsTable = lazy(() => import('./components/PointsTable'));
const AddMatchResult = lazy(() => import('./components/AddMatchResult'));

// Wrap in Suspense
<Suspense fallback={<CircularProgress />}>
  <PointsTable />
</Suspense>
```

### Memoization

```typescript
// Prevent unnecessary re-renders
const MemoizedPointsTable = React.memo(PointsTable);

// Use useMemo for expensive calculations
const sortedTable = useMemo(() => {
  return data.sort((a, b) => b.points - a.points);
}, [data]);
```

---

## Environment Variables

**File**: `.env`

```bash
# API Base URL
VITE_API_BASE_URL=http://localhost:8080

# Environment
VITE_ENV=development

# Feature flags
VITE_ENABLE_AI_FEATURES=false
```

**Usage in Code**:
```typescript
const apiUrl = import.meta.env.VITE_API_BASE_URL;
```

---

## Testing

### Unit Tests (Planned)

```bash
# Jest + React Testing Library
npm run test

# Coverage
npm run test:coverage
```

### E2E Tests (Planned)

```bash
# Playwright or Cypress
npm run test:e2e
```

---

## Common Tasks

### Add New Component

```bash
# Create component file
touch src/components/NewComponent.tsx

# Import in Router.tsx
import NewComponent from './components/NewComponent';

# Add route
<Route path="/new" element={<NewComponent />} />
```

### Add New API Endpoint

```bash
# Add to services/api.ts
export const newEndpoint = (params) => {
  return api.get(`/api/v1/new/${params}`);
};

# Use in component
const { data } = await newEndpoint(params);
```

---

## Future Enhancements

- **State Management**: Redux or Zustand for complex state
- **Real-Time Updates**: WebSocket integration for live leaderboards
- **PWA**: Progressive Web App with offline support
- **Performance Monitoring**: React DevTools Profiler integration
- **Accessibility**: WCAG compliance, screen reader support
- **i18n**: Multi-language support

---

[← Back to Main Documentation](../README.md)
