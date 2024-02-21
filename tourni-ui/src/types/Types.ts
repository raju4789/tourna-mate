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
  fullName: string;
  username: string;
  token: string;
  role?: Role;
}

export interface IRegisterResponse {
  fullName: string;
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

export interface ITeam {
  teamId: number;
  teamName: string;
}

export interface DropdownOption {
  value: string;
  label: string;
}

export type Anchor = 'top' | 'left' | 'bottom' | 'right';
export enum Role { ADMIN = 'ADMIN', USER = 'USER' }

export enum MatchResultStatus {
  TIED = 'TIED',
  COMPLETED = 'COMPLETED',
  NO_RESULT = 'NO_RESULT',
}

export interface IMatchResult {
  matchNumber: number;
  tournamentName: string;
  winnerTeamName: string;
  loserTeamName: string;
  teamOneName: string;
  teamTwoName: string;
  teamOneScore: number;
  teamTwoScore: number;
  teamOneWickets: number;
  teamTwoWickets: number;
  teamOneOversPlayed: number;
  teamTwoOversPlayed: number;
  matchResultStatus: MatchResultStatus;
}

export interface IAddMatchResultRequest {
  matchNumber: number;
  tournamentId: number;
  winnerTeamId: number;
  loserTeamId: number;
  teamOneId: number;
  teamTwoId: number;
  teamOneScore: number;
  teamTwoScore: number;
  teamOneWickets: number;
  teamTwoWickets: number;
  teamOneOversPlayed: number;
  teamTwoOversPlayed: number;
  matchResultStatus: MatchResultStatus;
}
