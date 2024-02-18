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
  username: string;
  token: string;
  role?: Role;
}

export interface IRegisterResponse {
  username: string;
  token: string;
  role?: Role;
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

export interface IPointsTableResponse {
  tournamentId: number;
  pointsTable: IPointstable[];
}

export interface IPointstable {
  teamName: string;
  played: number;
  won: number;
  lost: number;
  tied: number;
  noResult: number;
  points: number;
  netMatchRate: number;
}

export interface ITournament {
  tournamentId: number;
  tournamentName: string;
}

export interface DropdownOption {
  value: string;
  label: string;
}

export type Anchor = 'top' | 'left' | 'bottom' | 'right';
export enum Role { ADMIN = 'ADMIN', USER = 'USER' }
