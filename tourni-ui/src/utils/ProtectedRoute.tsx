import React from 'react';
import {
  Navigate, Outlet, useOutletContext,
} from 'react-router';
import { IAppOutletContext } from '../types/Types';

const ProtectedRoute: React.FC<{ role: string }> = ({ role }) => {
  const context = useOutletContext<IAppOutletContext>();

  if (!context.isAuthenticated || !context.roles.includes(role)) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet context={context} />;
};

export default ProtectedRoute;
