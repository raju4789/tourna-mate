import { Home } from '@mui/icons-material';
import { Root } from 'react-dom/client';
import { createBrowserRouter } from 'react-router-dom';
import Login from './components/login/Login';
import Signup from './components/signup/Signup';

const router = createBrowserRouter(
    [   { 
            path: '/', 
            element: <Root />,
            children: [
                {
                    path: '/',
                    element: <div>Home</div>,
                    index: true
                },
                {
                    path: '/pointsTable',
                    element: <div>Home</div>,
                },
                {
                    path: '/login',
                    element: <Login />,
                },
                {
                    path: '/signup',
                    element: <Signup />,
                },
                {
                    path: '/pointsTable',
                    element: <div>Private</div>,
                },
            ]
        },
        
    ]
);

export default router;
