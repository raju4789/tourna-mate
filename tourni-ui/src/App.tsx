import { HashRouter, Route, Routes } from 'react-router-dom';
import Login from './components/login/Login';
import Signup from './components/login/signup/Signup';

function App() {
  return (
    <div style={{ width: '100%' }}>
      <HashRouter>
        <Routes>
          <Route path="/" element={<div>Home</div>} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
        </Routes>
      </HashRouter>
    </div>

  );
}

export default App;
