/* eslint-disable max-len */
import React, { useState } from 'react';
import {
  FormControlLabel, Grid, Typography,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { CheckBox } from '@mui/icons-material';
import { useNavigate } from 'react-router';
import { Link } from 'react-router-dom';
import {
  StyledPaper, StyledAvatar, StyledTextField, StyledButton,
} from './Login.styled';
import LoginService from '../../services/LoginService';

const Login: React.FC = () => {
  const navigate = useNavigate();

  const [username, setUserName] = useState<string>('');
  const [password, setPassword] = useState<string>('');

  const handleUserNameChange = (event: React.ChangeEvent<HTMLInputElement>) => setUserName(event.target.value);
  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => setPassword(event.target.value);

  const [message, setMessage] = React.useState('');

  const handleSubmit = async () => {
    try {
      const loginCredentials = { username, password };
      const response = await LoginService.login(loginCredentials);
      const body = response.data;
      if (body.jwt) {
        localStorage.clear();
        localStorage.setItem('jwt', body.jwt);
        localStorage.setItem('userName', body.userName);
        navigate('/dashboard');
      } else {
        setMessage('Login failed');
      }
    } catch (error) {
      setMessage('Login failed');
    }

    setUserName('');
    setPassword('');
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
        <StyledTextField label="Username" placeholder="Enter username" fullWidth required onChange={handleUserNameChange} />
        <StyledTextField label="Password" placeholder="Enter password" type="password" fullWidth required onChange={handlePasswordChange} />
        <FormControlLabel
          label="Remember me"
          control={<CheckBox color="primary" />}
        />
        <StyledButton type="submit" color="primary" variant="contained" fullWidth onClick={handleSubmit}>
          Login
        </StyledButton>
        <Typography style={{ margin: 7, color: 'red' }} variant="body1">
          {message}
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
