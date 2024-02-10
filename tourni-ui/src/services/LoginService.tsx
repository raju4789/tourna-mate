import axios from 'axios';

const API_BASE_URL = '/user';

const headers = {
  'Content-Type': 'application/json',
  'Access-Control-Allow-Origin': '*',
};
const login = (loginCredentials: any) => axios.post(`${API_BASE_URL}/login`, loginCredentials, { headers });

const register = (user: { confirmPassword: string; password?: string; }) => {
  const { password, confirmPassword } = user;

  if (password !== confirmPassword) {
    throw new Error("Passwords don't match");
  }

  delete user.confirmPassword;

  return axios.post(`${API_BASE_URL}/register`, user, { headers });
};

export default { login, register };
