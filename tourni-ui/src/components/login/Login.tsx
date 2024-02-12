import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import {
  FormControlLabel, Grid, Typography, Checkbox,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { useNavigate } from 'react-router';
import { Link } from 'react-router-dom';
import { Axios, AxiosResponse } from 'axios';
import {
  StyledPaper, StyledAvatar, StyledTextField, StyledButton,
} from './Login.styled';
import {
  ICommonApiResponse, IErrorDetails, ILoginRequest, ILoginResponse,
} from '../../types/Types';
import { loginUser } from '../../services/LoginService';

const Login: React.FC = () => {
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors } } = useForm<ILoginRequest>({
    mode: 'onTouched', // Validation will trigger on the blur event
  });

  const onSubmit = async (data: ILoginRequest) => {
    try {
      const response: AxiosResponse<ICommonApiResponse<ILoginResponse>> = await loginUser(data);
      const body: ICommonApiResponse<ILoginResponse> = response.data;
      if (body.isSuccess) {
        localStorage.clear();
        localStorage.setItem('jwt', body.data.jwt);
        localStorage.setItem('role', body.data.role || '');
        navigate('/pointsTable');
      } else {
        const errorDetails: IErrorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        setErrorMessage(`Login failed with error: ${errorDetails.errorMessage}. Please try again.`);
      }
    } catch (error) {
      console.error('Login failed ', error);
      setErrorMessage('Login failed with unknown error. Please try again.');
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
          {errorMessage}
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
