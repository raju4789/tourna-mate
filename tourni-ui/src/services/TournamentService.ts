import axios, { AxiosResponse } from 'axios';
import { ICommonApiResponse, IPointsTableResponse, ITournament } from '../types/Types';

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
