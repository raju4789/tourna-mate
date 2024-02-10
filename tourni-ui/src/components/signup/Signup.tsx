/* eslint-disable max-len */
import React, { useState } from 'react';
import {
  Grid, Typography,
} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { StyledAvatar, StyledPaper } from './Signup.styled';
import LoginService from '../../../services/LoginService';
import { User } from '../../../types/Types';
import logger from '../../../../logger';
import { StyledButton, StyledTextField } from '../Login.styled';

const Signup: React.FC = () => {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState<string>('');
  const [lastName, setLastName] = useState<string>('');
  const [username, setUserName] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [message, setMessage] = useState<string>('');

  const handleFirstNameChange = (event: React.ChangeEvent<HTMLInputElement>) => setFirstName(event.target.value);
  const handleLastNameChange = (event: React.ChangeEvent<HTMLInputElement>) => setLastName(event.target.value);
  const handleUserNameChange = (event: React.ChangeEvent<HTMLInputElement>) => setUserName(event.target.value);
  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => setPassword(event.target.value);
  const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => setConfirmPassword(event.target.value);
  const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => setEmail(event.target.value);

  const handleSubmit = async () => {
    try {
      const user: User = {
        username, password, confirmPassword, firstName, lastName, email,
      };
      const response = await LoginService.register(user);
      if (response.status !== 201) {
        setMessage('Failed to register');
      }
      navigate('/');
    } catch (error) {
      logger.error('Failed to register', error);
      setMessage('Failed to register');
    }
  };

  return (
    <Grid container justifyContent="center" alignItems="center" direction="column" style={{ height: '100vh' }}>
      <StyledPaper elevation={10} style={{ padding: '20px', width: '280px', margin: '20px' }}>
        <Grid container justifyContent="center" alignItems="center" direction="column">
          <StyledAvatar>
            <LockOutlinedIcon />
          </StyledAvatar>
          <Typography variant="h5">Signup</Typography>
        </Grid>
        <form>
          <Grid container justifyContent="center" alignItems="center" direction="column">
            <StyledTextField label="First name" placeholder="Enter First name" fullWidth required value={firstName} onChange={handleFirstNameChange} />
            <StyledTextField label="Last name" placeholder="Enter Last name" fullWidth required value={lastName} onChange={handleLastNameChange} />
          </Grid>
          <StyledTextField label="Username" placeholder="Enter username" fullWidth required value={username} onChange={handleUserNameChange} />
          <StyledTextField label="Email" placeholder="Enter email" fullWidth required value={email} onChange={handleEmailChange} />
          <StyledTextField label="Password" placeholder="Enter password" type="password" fullWidth required value={password} onChange={handlePasswordChange} />
          <StyledTextField label="Confirm Password" placeholder="Confirm password" type="password" fullWidth required value={confirmPassword} onChange={handleConfirmPasswordChange} />
          <StyledButton fullWidth variant="contained" color="primary" onClick={handleSubmit}>
            Sign Up
          </StyledButton>
          <Typography style={{ margin: 7, color: 'red' }} variant="body1">
            {message}
          </Typography>
        </form>
        <Typography>
          Already have an account?
          {' '}
          <Link to="/login">Login</Link>
        </Typography>
      </StyledPaper>
    </Grid>
  );
};

export default Signup;
