import React from 'react';
import {
  Navigate, Outlet,
} from 'react-router';
import useLocalStorage from '../hooks/useLocalStorage';
import { hasRole } from './roleHelpers';

const ProtectedRoute: React.FC<{ role: string }> = ({ role }) => {
  const { getItem: getIsAuthenticated } = useLocalStorage('isAuthenticated');
  const { getItem: getRole } = useLocalStorage('role');

  const userRoles = getRole() as string | null;
  
  if (!getIsAuthenticated() || !hasRole(userRoles, role)) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
