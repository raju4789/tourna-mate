import React from 'react';
import { useNavigate } from 'react-router';
import {
  StyledAppBar, StyledToolbar, StyledTypography, StyledButton,
} from './Header.styled';
import usePersistedState from '../../hooks/usePersistedState';

const Header: React.FC = () => {
  const navigate = useNavigate();

  const [, setJwt] = usePersistedState('jwt', '');
  const [isAuthenticated, setIsAuthenticated] = usePersistedState('isAuthenticated', false);
  const [userName, setUserName] = usePersistedState('username', '');
  const [roles, setRoles] = usePersistedState('roles', ['admin']);

  const onLogout = () => {
    setJwt('');
    setIsAuthenticated(false);
    setRoles([]);
    setUserName('');
    navigate('/login');
  };

  const onManageClick = () => {
    navigate('/manageTournament');
  };

  return (
    <StyledAppBar position="static" color="default" elevation={0}>
      <StyledToolbar>
        <StyledTypography variant="h6" color="inherit" noWrap>
          Tournamate
        </StyledTypography>
        {isAuthenticated ? (
          <StyledTypography variant="h6" color="inherit" noWrap>
            Welcome
            {' '}
            {userName}
          </StyledTypography>
        ) : null}
        {isAuthenticated && roles?.includes('admin') ? (
          <StyledButton
            color="secondary"
            variant="outlined"
            onClick={onManageClick}
          >
            Manage
          </StyledButton>
        ) : null}
        {isAuthenticated ? (
          <StyledButton
            color="secondary"
            variant="outlined"
            onClick={onLogout}
          >
            Logout
          </StyledButton>
        ) : null}
      </StyledToolbar>
    </StyledAppBar>
  );
};

export default Header;
