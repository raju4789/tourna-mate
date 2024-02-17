import React from 'react';
import { useForm } from 'react-hook-form';
import {
  Grid, Typography, Checkbox, FormControlLabel,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { Link, useNavigate } from 'react-router-dom';
import { AxiosResponse } from 'axios';
import {
  StyledAvatar, StyledButton, StyledPaper, StyledTextField,
} from './Signup.styled';
import {
  ICommonApiResponse,
  IErrorDetails,
  IRegisterResponse,
  ISignupRequest,
} from '../../types/Types';
import usePersistedState from '../../hooks/usePersistedState';
import { registerUser } from '../../services/LoginService';

const Signup: React.FC = () => {
  const navigate = useNavigate();
  const [apiErrorMessage, setAPIErrorMessage] = React.useState<string>('');
  const [, setJwt] = usePersistedState('jwt', '');
  const [, setIsAuthenticated] = usePersistedState('isAuthenticated', false);
  const [, setUserName] = usePersistedState('username', '');
  const [, setRoles] = usePersistedState('roles', ['admin']);
  const {
    register, handleSubmit, formState: { errors },
  } = useForm<ISignupRequest>({
    mode: 'onTouched', // Validation will trigger on the blur event
  });

  const onSignup = async (data: ISignupRequest) => {
    try {
      const { password, confirmPassword } = data;
      if (password !== confirmPassword) {
        setAPIErrorMessage("Passwords don't match");
        return;
      }
      const response: AxiosResponse<ICommonApiResponse<IRegisterResponse>> = await registerUser(data);
      const body: ICommonApiResponse<IRegisterResponse> = response.data;
      if (body.success) {
        setUserName(data.username);
        setJwt(body.data.jwt);
        setIsAuthenticated(true);
        setRoles([body.data.role]);
        navigate('/pointsTable');
      } else {
        const errorDetails: IErrorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        setAPIErrorMessage(`Registration failed with error ${errorDetails.errorMessage} Please try again.`);
      }
    } catch (error) {
      console.error('Registration failed', error);
      setAPIErrorMessage('Registration failed with unknown error. Please try again.');
    }
  };

  return (
    <Grid container justifyContent="center" alignItems="center" style={{ height: '100vh' }}>
      <StyledPaper elevation={10}>
        <Grid container justifyContent="center" alignItems="center" direction="column">
          <StyledAvatar>
            <LockOutlinedIcon />
          </StyledAvatar>
          <Typography variant="h5">Signup</Typography>
        </Grid>
        <form onSubmit={handleSubmit(onSignup)}>
          <StyledTextField
            label="First name"
            placeholder="Enter First name"
            fullWidth
            required
            {...register('firstName', {
              required: 'First name is required',
            })}
            error={!!errors.firstName}
            helperText={errors.firstName?.message}
          />
          <StyledTextField
            label="Last name"
            placeholder="Enter Last name"
            fullWidth
            required
            {...register('lastName', {
              required: 'Last name is required',
            })}
            error={!!errors.lastName}
            helperText={errors.lastName?.message}
          />
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
            label="Email"
            type="email"
            placeholder="Enter email"
            fullWidth
            required
            {...register('email', {
              required: 'Email is required',
              pattern: {
                value: /\S+@\S+\.\S+/,
                message: 'Invalid email address',
              },
            })}
            error={!!errors.email}
            helperText={errors.email?.message}
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
          <StyledTextField
            label="Confirm Password"
            placeholder="Confirm password"
            type="password"
            fullWidth
            required
            {...register('confirmPassword', {
              required: 'Confirm password is required',
            })}
            error={!!errors.confirmPassword}
            helperText={errors.confirmPassword?.message}
          />
          <FormControlLabel
            label="Remember me"
            control={<Checkbox color="primary" />}
          />
          <StyledButton type="submit" color="primary" variant="contained" fullWidth>
            Signup
          </StyledButton>
        </form>
        <Typography style={{ margin: 7, color: 'red' }} variant="body1">
          {apiErrorMessage}
        </Typography>
        <Typography>
          Do you have an account?
          {' '}
          <Link to="/login">Login</Link>
        </Typography>
      </StyledPaper>
    </Grid>
  );
};

export default Signup;
