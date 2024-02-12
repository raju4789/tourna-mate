import axios from 'axios';
import {
  ILoginRequest, IRegistrationRequest, ISignupRequest,
} from '../types/Types';

const API_BASE_URL: string = import.meta.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1/auth';

const headers = {
  'Content-Type': 'application/json',
  'Access-Control-Allow-Origin': '*',
};

export const loginUser = (loginCredentials: ILoginRequest) => axios.post(`${API_BASE_URL}/authenticate`, loginCredentials, { headers });

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
