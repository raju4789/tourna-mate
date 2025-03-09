import React from 'react';
import {
  Navigate, Outlet,
} from 'react-router';
import useLocalStorage from '../hooks/useLocalStorage';

const ProtectedRoute: React.FC<{ role: string }> = ({ role }) => {
  const { getItem: getIsAuthenticated } = useLocalStorage('isAuthenticated');
  const { getItem: getRole } = useLocalStorage('role');

  if (!getIsAuthenticated() || !(getRole() === role)) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
