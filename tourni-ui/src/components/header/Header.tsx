import React from 'react';
import { useNavigate } from 'react-router';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import Box from '@mui/material/Box';
import {
  StyledAppBar, StyledToolbar, StyledTypography, StyledButton,
} from './Header.styled';
import useLocalStorage from '../../hooks/useLocalStorage';
import Sidebar from '../sidebar/Sidebar';
import { Anchor } from '../../types/Types';

const Header: React.FC = () => {
  const navigate = useNavigate();

  const { removeItem: removeJwt } = useLocalStorage('jwt' as string);
  const { getItem: getIsAuthenticated, removeItem: removeIsAuthenticated } = useLocalStorage('isAuthenticated' as string);
  const { getItem: getUserName, removeItem: removeUsername } = useLocalStorage('username' as string);
  const { removeItem: removeRole } = useLocalStorage('role' as string);

  const [sideBarDirection, setSidebarDirection] = React.useState({
    left: false,

  });

  const toggleDrawer = (anchor: Anchor, open: boolean) => (event: React.KeyboardEvent | React.MouseEvent) => {
    if (
      event.type === 'keydown'
        && ((event as React.KeyboardEvent).key === 'Tab'
          || (event as React.KeyboardEvent).key === 'Shift')
    ) {
      return;
    }

    setSidebarDirection({ ...sideBarDirection, [anchor]: open });
  };

  const onLogout = () => {
    removeJwt();
    removeIsAuthenticated();
    removeUsername();
    removeRole();
    navigate('/login');
  };

  const onLogin = () => {
    navigate('/login');
  };

  return (
    <Box>
      <StyledAppBar position="static" color="default" elevation={0}>
        <StyledToolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
            onClick={toggleDrawer('left', true)}
          >
            <MenuIcon />
          </IconButton>
          <StyledTypography variant="h6" color="inherit" noWrap>
            Tournamate
          </StyledTypography>
          {getIsAuthenticated() ? (
            <StyledTypography variant="h6" color="inherit" noWrap>
              Welcome
              {' '}
              {getUserName()}
            </StyledTypography>
          ) : null}
          {getIsAuthenticated() ? (
            <StyledButton
              color="secondary"
              variant="outlined"
              onClick={onLogout}
            >
              Logout
            </StyledButton>
          ) : null}
          {!getIsAuthenticated() ? (
            <StyledButton
              color="secondary"
              variant="outlined"
              onClick={onLogin}
            >
              Login
            </StyledButton>
          ) : null}
        </StyledToolbar>
      </StyledAppBar>
      <Sidebar sideBarDirection={sideBarDirection} toggleDrawer={toggleDrawer} />
    </Box>
  );
};

export default Header;
