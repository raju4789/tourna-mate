import {
  AppBar, Toolbar, Typography, Button, styled, Box,
} from '@mui/material';

export const StyledAppBar = styled(AppBar)(({ theme }) => ({
  backgroundColor: theme.palette.primary.main,
  color: 'white',
  left: 0,
  top: 0,
  textAlign: 'center',
  position: 'fixed',
}));

export const StyledToolbar = styled(Toolbar)`
  flex-wrap: wrap;
`;

export const StyledTypography = styled(Typography)`
`;

export const StyledAppName = styled(Typography)`
  position: absolute;
  left: 5%;
`;

export const StyledButton = styled(Button)`
  border: 1px solid white;
  background-color: white;
  color: ${({ theme }) => theme.palette.primary.main};
  margin-right: 0px;


  &:hover {
    background-color: ${({ theme }) => theme.palette.primary.main};
    color: white;
    border: 1px solid white;
  }
`;

export const AuthenticatedSection = styled(Box)`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    gap: 10px;
`;
