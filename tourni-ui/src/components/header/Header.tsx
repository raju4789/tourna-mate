import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  StyledAppBar, StyledToolbar, StyledTypography, StyledButton,
} from './Header.styled';

const Header: React.FC = () => {
  const navigate = useNavigate();

  const isAuthenticated: boolean = localStorage.getItem('jwt') !== null;

  const userName = localStorage.getItem('userName');

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
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
        {isAuthenticated ? (
          <StyledButton
            color="secondary"
            variant="outlined"
            onClick={handleLogout}
          >
            Logout
          </StyledButton>
        ) : null}
      </StyledToolbar>
    </StyledAppBar>
  );
};

export default Header;
