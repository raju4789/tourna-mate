import React from 'react';
import { useNavigate } from 'react-router';
import {
  StyledAppBar, StyledToolbar, StyledTypography, StyledButton,
} from './Header.styled';
import { IHeaderProps } from '../../types/Types';

const Header: React.FC<IHeaderProps> = (props) => {
  const navigate = useNavigate();
  const {
    onLogout, isAuthenticated, userName, roles,
  } = props;

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
        {roles?.includes('admin') ? (
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
