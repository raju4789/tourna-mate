import axios, { AxiosResponse } from 'axios';
import { parse } from 'path';
import {
  IAddMatchResultRequest,
  ICommonApiResponse, IMatchResult, IPointsTableResponse, ITeam, ITournament,
} from '../types/Types';

const headers = {
  'Content-Type': 'application/json',
  'Access-Control-Allow-Origin': '*',
};

const axiosInstance = axios.create({
  baseURL: '/api/v1/manage',
  headers,
});

export const getPointsTable = (tournamentId: number): Promise<AxiosResponse<ICommonApiResponse<IPointsTableResponse>>> => axiosInstance.get(`/pointstable/tournament/${tournamentId}`);

export const getAllTournaments = (): Promise<AxiosResponse<ICommonApiResponse<ITournament[]>>> => axiosInstance.get('/tournaments');

export const getAllTeams = (tournamentId: number): Promise<AxiosResponse<ICommonApiResponse<ITeam[]>>> => axiosInstance.get(`/teams?tournamentId=${tournamentId}`);

export const addMatchResult = (data: IMatchResult)
: Promise<AxiosResponse<ICommonApiResponse<string>>> => {
  const addMatchResultRequest: IAddMatchResultRequest = {
    matchNumber: data.matchNumber,
    tournamentId: parseInt(data.tournamentName, 10),
    winnerTeamId: parseInt(data.winnerTeamName, 10),
    loserTeamId: parseInt(data.loserTeamName, 10),
    teamOneId: parseInt(data.teamOneName, 10),
    teamTwoId: parseInt(data.teamTwoName, 10),
    teamOneScore: data.teamOneScore,
    teamTwoScore: data.teamTwoScore,
    teamOneWickets: data.teamOneWickets,
    teamTwoWickets: data.teamTwoWickets,
    teamOneOversPlayed: data.teamOneOversPlayed,
    teamTwoOversPlayed: data.teamTwoOversPlayed,
    matchResultStatus: data.matchResultStatus,
  };

  return axiosInstance.post('/addMatchResult', addMatchResultRequest);
};
