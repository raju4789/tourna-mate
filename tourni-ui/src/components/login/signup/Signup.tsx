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
  const [errors, setErrors] = useState<Partial<User>>({});

  const handleFirstNameChange = (event: React.ChangeEvent<HTMLInputElement>) => setFirstName(event.target.value);
  const handleLastNameChange = (event: React.ChangeEvent<HTMLInputElement>) => setLastName(event.target.value);
  const handleUserNameChange = (event: React.ChangeEvent<HTMLInputElement>) => setUserName(event.target.value);
  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => setPassword(event.target.value);
  const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => setConfirmPassword(event.target.value);
  const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => setEmail(event.target.value);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault(); // Prevent default form submission behavior

    // Reset error message
    setMessage('');

    // Perform form validation
    const errors: Partial<User> = {};
    if (!firstName.trim()) {
      errors.firstName = 'First name is required';
    }
    if (!lastName.trim()) {
      errors.lastName = 'Last name is required';
    }
    if (!username.trim()) {
      errors.username = 'Username is required';
    }
    if (!email.trim()) {
      errors.email = 'Email is required';
    } else if (!/^\S+@\S+\.\S+$/.test(email)) {
      errors.email = 'Invalid email format';
    }
    if (!password.trim()) {
      errors.password = 'Password is required';
    } else if (password.length < 6) {
      errors.password = 'Password must be at least 6 characters';
    }
    if (password !== confirmPassword) {
      errors.confirmPassword = 'Passwords do not match';
    }

    // Set errors if any
    if (Object.keys(errors).length > 0) {
      setErrors(errors);
      return; // Exit early if there are validation errors
    }

    // If no validation errors, proceed with form submission
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
        <form onSubmit={handleSubmit}>
          <Grid container justifyContent="center" alignItems="center" direction="column">
            <StyledTextField
              label="First name"
              placeholder="Enter First name"
              fullWidth
              required
              value={firstName}
              onChange={handleFirstNameChange}
              error={!!errors.firstName}
              helperText={errors.firstName}
            />
            <StyledTextField label="Last name" placeholder="Enter Last name" fullWidth required value={lastName} onChange={handleLastNameChange} error={!!errors.lastName} helperText={errors.lastName} />
            <StyledTextField label="Username" placeholder="Enter username" fullWidth required value={username} onChange={handleUserNameChange} error={!!errors.username} helperText={errors.username} />
            <StyledTextField label="Email" placeholder="Enter email" fullWidth required value={email} onChange={handleEmailChange} error={!!errors.email} helperText={errors.email} />
            <StyledTextField label="Password" placeholder="Enter password" type="password" fullWidth required value={password} onChange={handlePasswordChange} error={!!errors.password} helperText={errors.password} />
            <StyledTextField label="Confirm Password" placeholder="Confirm password" type="password" fullWidth required value={confirmPassword} onChange={handleConfirmPasswordChange} error={!!errors.confirmPassword} helperText={errors.confirmPassword} />
            <StyledButton type="submit" fullWidth variant="contained" color="primary">
              Sign Up
            </StyledButton>
            <Typography style={{ margin: 7, color: 'red' }} variant="body1">
              {message}
            </Typography>
          </Grid>
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
