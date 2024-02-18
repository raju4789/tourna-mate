import { createBrowserRouter } from 'react-router-dom';
import Login from './components/login/Login';
import Signup from './components/signup/Signup';
import Root from './components/root/Root';
import ProtectedRoute from './utils/ProtectedRoute';
import PointsTable from './components/pointstable/PointsTable';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Root />,
    children: [
      {
        index: true,
        element: <Login />,
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
        element: <PointsTable />,
      },
      {
        element: <ProtectedRoute role="ADMIN" />,
        children: [
          {
            path: 'manageTournament',
            element: <div> Tournament </div>,
          },
        ],
      },
    ],
  },
]);

export default router;
