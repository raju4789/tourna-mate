import { createBrowserRouter } from 'react-router-dom';
import Login from './components/login/Login';
import Signup from './components/signup/Signup';
import Root from './components/root/Root';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Root />,
    children: [
      {
        index: true,
        element: <Root />,
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
