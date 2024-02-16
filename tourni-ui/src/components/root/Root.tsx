import React from 'react';
import { Outlet, useNavigate } from 'react-router';
import { AxiosResponse } from 'axios';
import Header from '../header/Header';
import Footer from '../footer/Footer';
import { loginUser, registerUser } from '../../services/LoginService';
import {
  ILoginRequest, ICommonApiResponse, ILoginResponse, IErrorDetails, IRegisterResponse, ISignupRequest, IAppOutletContext,
} from '../../types/Types';

const Root: React.FC = () => {
  const navigate = useNavigate();

  const [apiErrorMessage, setAPIErrorMessage] = React.useState<string>('');
  const [jwt, setJwt] = React.useState<string>('');
  const [isAuthenticated, setIsAuthenticated] = React.useState<boolean>(false);
  const [userName, setUserName] = React.useState<string>('');
  const [roles, setRoles] = React.useState<string[]>([]);

  const onLogin = async (data: ILoginRequest) => {
    try {
      const response: AxiosResponse<ICommonApiResponse<ILoginResponse>> = await loginUser(data);
      const body: ICommonApiResponse<ILoginResponse> = response.data;
      if (body.success) {
        setUserName(data.username);
        setJwt(body.data.jwt);
        setIsAuthenticated(true);
        setRoles(body.data.role ? [body.data.role] : ['admin']);

        navigate('/pointsTable');
      } else {
        const errorDetails: IErrorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        setIsAuthenticated(false);
        setAPIErrorMessage(`Login failed with error: ${errorDetails.errorMessage}. Please try again.`);
      }
    } catch (error) {
      console.error('Login failed ', error);
      setIsAuthenticated(true);
      setAPIErrorMessage('Login failed with unknown error. Please try again.');
    }
  };

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
        setRoles(body.data.role ? [body.data.role] : []);
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

  const onLogout = () => {
    setJwt('');
    setIsAuthenticated(false);
    setRoles([]);
    setUserName('');
    navigate('/login');
  };

  const appOutletContext: IAppOutletContext = {
    userName, jwt, isAuthenticated, roles, onLogin, onSignup, onLogout, apiErrorMessage,
  };

  return (
    <>
      <Header userName={userName} isAuthenticated={isAuthenticated} roles={roles} onLogout={onLogout} />
      <Outlet context={appOutletContext} />
      <Footer />
    </>
  );
};

export default Root;
