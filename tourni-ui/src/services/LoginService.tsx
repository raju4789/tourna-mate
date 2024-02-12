import axios from 'axios';
import {
  ILoginRequest, IRegistrationRequest, ISignupRequest,
} from '../types/Types';

const API_BASE_URL: string = import.meta.env.VITE_REACT_APP_API_URL || 'http://localhost:8080';
console.log('API_BASE_URL: ', API_BASE_URL);
const headers = {
  'Content-Type': 'application/json',
  'Access-Control-Allow-Origin': '*',
};

export const loginUser = (loginCredentials: ILoginRequest) => axios.post('/api/v1/auth/authenticate', loginCredentials, { headers });

export const registerUser = (user: ISignupRequest) => {
  const registrationRequest: IRegistrationRequest = {
    username: user.username,
    email: user.email,
    password: user.password,
    firstName: user.firstName,
    lastName: user.lastName,
  };

  return axios.post(`${API_BASE_URL}/register`, registrationRequest, { headers });
};
