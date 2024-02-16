import React from 'react';
import { useForm } from 'react-hook-form';
import {
  FormControlLabel, Grid, Typography, Checkbox,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { useOutletContext } from 'react-router';
import { Link } from 'react-router-dom';
import {
  StyledPaper, StyledAvatar, StyledTextField, StyledButton,
} from './Login.styled';
import {
  IAppOutletContext,
  ILoginRequest,
} from '../../types/Types';

const Login: React.FC = () => {
  const { onLogin, apiErrorMessage } = useOutletContext<IAppOutletContext>();

  const { register, handleSubmit, formState: { errors } } = useForm<ILoginRequest>({
    mode: 'onTouched',
  });

  return (
    <Grid container justifyContent="center" alignItems="center" style={{ height: '100vh', width: '100%' }}>
      <StyledPaper elevation={10}>
        <Grid container justifyContent="center" alignItems="center" direction="column">
          <StyledAvatar>
            <LockOutlinedIcon />
          </StyledAvatar>
          <Typography variant="h5">Login</Typography>
        </Grid>
        <form onSubmit={handleSubmit(onLogin)}>
          <StyledTextField
            label="Username"
            placeholder="Enter username"
            fullWidth
            required
            {...register('username', {
              required: 'Username is required',
            })}
            error={!!errors.username}
            helperText={errors.username?.message}
          />
          <StyledTextField
            label="Password"
            placeholder="Enter password"
            type="password"
            fullWidth
            required
            {...register('password', {
              required: 'Password is required',
            })}
            error={!!errors.password}
            helperText={errors.password?.message}
          />
          <FormControlLabel
            label="Remember me"
            control={<Checkbox color="primary" />}
          />
          <StyledButton type="submit" color="primary" variant="contained" fullWidth>
            Login
          </StyledButton>
        </form>
        <Typography style={{ margin: 7, color: 'red' }} variant="body1">
          {apiErrorMessage}
        </Typography>
        <Typography>
          Do you have an account?
          {' '}
          <Link to="/signup">Sign up</Link>
        </Typography>
      </StyledPaper>
    </Grid>
  );
};

export default Login;
