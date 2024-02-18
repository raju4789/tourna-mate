import React from 'react';
import { useForm } from 'react-hook-form';
import {
  FormControlLabel, Grid, Typography, Checkbox,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { useNavigate } from 'react-router';
import { Link } from 'react-router-dom';
import { AxiosResponse } from 'axios';
import {
  StyledPaper, StyledAvatar, StyledTextField, StyledButton,
} from './Login.styled';
import {
  ICommonApiResponse,
  IErrorDetails,
  ILoginRequest,
  ILoginResponse,
  Role,
} from '../../types/Types';
import { loginUser } from '../../services/LoginService';
import useLocalStorage from '../../hooks/useLocalStorage';

const Login: React.FC = () => {
  const [apiErrorMessage, setAPIErrorMessage] = React.useState<string>('');
  const { setItem: setJwt } = useLocalStorage('jwt' as string);
  const { setItem: setIsAuthenticated } = useLocalStorage('isAuthenticated' as string);
  const { setItem: setUserName } = useLocalStorage('username' as string);
  const { setItem: setRole } = useLocalStorage('role' as string);

  const navigate = useNavigate();

  const onLogin = async (data: ILoginRequest) => {
    try {
      const response: AxiosResponse<ICommonApiResponse<ILoginResponse>> = await loginUser(data);
      const body: ICommonApiResponse<ILoginResponse> = response.data;
      if (body.success) {
        setJwt(body.data.token as string);
        setUserName(data.username as string);
        setIsAuthenticated(true as boolean);
        setRole(body.data.role as Role);

        navigate('/pointsTable');
      } else {
        setIsAuthenticated(false as boolean);
        const errorDetails: IErrorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        setAPIErrorMessage(`Login failed with error: ${errorDetails.errorMessage}. Please try again.`);
      }
    } catch (error) {
      console.error('Login failed ', error);
      setIsAuthenticated(false as boolean);
      setAPIErrorMessage('Login failed with unknown error. Please try again.');
    }
  };

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
