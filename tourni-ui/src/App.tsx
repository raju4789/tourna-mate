import {
  RouterProvider,
} from 'react-router-dom';
import router from './Router';

const App: React.FC = () => (
  <RouterProvider router={router} />
);

export default App;
