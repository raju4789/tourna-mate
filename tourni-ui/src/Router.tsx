import { createBrowserRouter } from 'react-router-dom';
import Login from './components/login/Login';
import Signup from './components/signup/Signup';
import Root from './components/root/Root';
import PointsTable from './components/pointstable/PointsTable';
import AddMatchResult from './components/addmatchresult/AddMatchResult';

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
        path: 'addMatchResult',
        element: <AddMatchResult />,
      },
    ],
  },
]);

export default router;
