import {
  Paper, Avatar, TextField, Button, styled,
} from '@mui/material';

export const StyledPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(4),
  height: '70vh',
  width: '380px',
  margin: '20px auto',
}));

export const StyledAvatar = styled(Avatar)(({ theme }) => ({
  backgroundColor: theme.palette.primary.main,
}));

export const StyledTextField = styled(TextField)(({ theme }) => ({
  margin: theme.spacing(1, 0),
}));

export const StyledButton = styled(Button)(({ theme }) => ({
  margin: theme.spacing(1, 0),
}));
