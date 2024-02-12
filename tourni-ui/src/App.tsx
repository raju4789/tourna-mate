/* eslint-disable max-len */
import {
  RouterProvider,
} from 'react-router-dom';
import router from './Router.tsx';

function App() {
  return (
    <RouterProvider router={router} />
  );
}

export default App;
