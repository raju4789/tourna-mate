import axios, { AxiosResponse } from 'axios';
import {
  ICommonApiResponse,
  ILoginRequest, ILoginResponse, IRegisterResponse, IRegistrationRequest, ISignupRequest,
} from '../types/Types';

const headers = {
  'Content-Type': 'application/json',
  'Access-Control-Allow-Origin': '*',
};

const axiosInstance = axios.create({
  baseURL: '/api/v1/auth',
  headers,
});

export const loginUser = (loginCredentials: ILoginRequest): Promise<AxiosResponse<ICommonApiResponse<ILoginResponse>>> => axiosInstance.post('/authenticate', loginCredentials, { headers });

export const registerUser = (user: ISignupRequest): Promise<AxiosResponse<ICommonApiResponse<IRegisterResponse>>> => {
  const registrationRequest: IRegistrationRequest = {
    username: user.username,
    email: user.email,
    password: user.password,
    firstName: user.firstName,
    lastName: user.lastName,
  };

  return axiosInstance.post('/register', registrationRequest, { headers });
};
