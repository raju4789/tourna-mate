import {
  AppBar, Toolbar, Typography, Button, styled,
} from '@mui/material';

export const StyledAppBar = styled(AppBar)(({ theme }) => ({
  backgroundColor: theme.palette.primary.main,
  color: 'white',
  left: 0,
  bottom: 0,
  marginBottom: 0,
  position: 'fixed',
  textAlign: 'center',
}));

export const StyledToolbar = styled(Toolbar)`
    flex-wrap: wrap;
`;

export const StyledTypography = styled(Typography)`
    flex-grow: 1;
`;

export const StyledButton = styled(Button)`
    margin: ${(props) => props.theme.spacing(1, 1.5)};
    color: white;
    border: none;
`;
