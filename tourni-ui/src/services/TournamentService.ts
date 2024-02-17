import axios from 'axios';

export const getPointsTable = (tournamentId: number) => axios.get(`/api/v1/manage/pointstable/tournament/${tournamentId}`);

export const getAllTournaments = () => axios.get('/api/v1/manage/tournaments');
