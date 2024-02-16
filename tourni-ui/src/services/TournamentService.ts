import axios from 'axios';

const getPointsTable = (tournamentId: number) => axios.get(`/api/v1/tournimanager/pointsTable/${tournamentId}`);

export default getPointsTable;
