export interface ISignupRequest {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}

export interface ILoginRequest {
  username: string;
  password: string;
}

export interface IRegistrationRequest {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
}

export interface IErrorDetails {
  errorCode: number;
  errorMessage: string;
}

export interface ICommonApiResponse<T> {
  success: boolean;
  data: T;
  errorDetails: IErrorDetails | null;
}

export interface ILoginResponse {
  jwt: string;
  role?: string;
}

export interface IRegisterResponse {
  jwt: string;
  role?: string;
}

export interface IAppOutletContext {
  userName: string;
  jwt: string;
  isAuthenticated: boolean;
  roles: string[];
  onLogin: (data: ILoginRequest) => Promise<void>;
  onSignup: (data: ISignupRequest) => Promise<void>;
  onLogout: () => void;
  apiErrorMessage: string;
}

export interface IHeaderProps {
  onLogout: () => void;
  isAuthenticated: boolean;
  userName: string;
  roles?: string[];
}

export interface IPoinstableResponse {
  teamName: string;
  played: number;
  won: number;
  lost: number;
  tied: number;
  noResult: number;
  points: number;
  netMatchRate: number;
}
