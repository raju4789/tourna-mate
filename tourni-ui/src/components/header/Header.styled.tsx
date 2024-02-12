import {
  AppBar, Toolbar, Typography, Button, styled,
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
  flex-grow: 1;
  float: right;
`;

export const StyledButton = styled(Button)`
  margin: ${({ theme }) => theme.spacing(1, 1.5)};
  border: 1px solid white;
  background-color: #3f51b5;
  color: white;
`;
