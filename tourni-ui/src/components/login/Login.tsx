import React from 'react';
import { useForm } from 'react-hook-form';
import {
  FormControlLabel, Grid, Typography, Checkbox,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { useNavigate } from 'react-router';
import { Link } from 'react-router-dom';
import {
  StyledPaper, StyledAvatar, StyledTextField, StyledButton,
} from './Login.styled';
import LoginService from '../../services/LoginService';

interface ILoginFormInput {
  username: string;
  password: string;
}

const Login: React.FC = () => {
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors } } = useForm<ILoginFormInput>();

  const onSubmit = async (data: ILoginFormInput) => {
    try {
      const response = await LoginService.login(data);
      const body = response.data;
      if (body.jwt) {
        localStorage.clear();
        localStorage.setItem('jwt', body.jwt);
        localStorage.setItem('userName', body.userName);
        navigate('/dashboard');
      } else {
        // Handle login failure
      }
    } catch (error) {
      // Handle login error
    }
  };

  return (
    <Grid container justifyContent="center" alignItems="center" style={{ height: '100vh' }}>
      <StyledPaper elevation={10}>
        <Grid container justifyContent="center" alignItems="center" direction="column">
          <StyledAvatar>
            <LockOutlinedIcon />
          </StyledAvatar>
          <Typography variant="h5">Login</Typography>
        </Grid>
        <form onSubmit={handleSubmit(onSubmit)}>
          <StyledTextField
            label="Username"
            placeholder="Enter username"
            fullWidth
            required
            {...register('username', { required: 'Username is required' })}
            error={!!errors.username}
            helperText={errors.username?.message}
          />
          <StyledTextField
            label="Password"
            placeholder="Enter password"
            type="password"
            fullWidth
            required
            {...register('password', { required: 'Password is required' })}
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
          {/* Display login error message here if needed */}
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