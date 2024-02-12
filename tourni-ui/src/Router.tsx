import { createBrowserRouter } from 'react-router-dom';
import Login from './components/login/Login';
import Signup from './components/signup/Signup';
import RootComponent from './components/root/RootComponent';
const router = createBrowserRouter([
  {
    path: '/',
    element: <RootComponent />,
    children: [
      {
        index: true,
        element: <RootComponent />,
      },
      {
        path: 'login',
        element: <Login />,
      },
      {
        path: 'signup',
        element: <Signup />,
      },
      {
        path: 'pointsTable',
        element: <div> Home </div>,
      },
    ],
  },
]);

export default router;