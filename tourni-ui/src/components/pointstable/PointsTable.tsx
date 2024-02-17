import React, { useMemo } from 'react';
import {
  Box, Typography, MenuItem, Select, InputLabel,
} from '@mui/material';

import { DataGrid } from '@mui/x-data-grid';
import { AxiosResponse } from 'axios';
import {
  ICommonApiResponse, IPointsTableResponse, IPointstable, ITournament,
} from '../../types/Types';
import { getPointsTable, getAllTournaments } from '../../services/TournamentService';

const PointsTable: React.FC = () => {
  const [apiErrorMessage, setAPIErrorMessage] = React.useState<string>('');
  const [pointsTable, setPointsTable] = React.useState<IPointstable[]>([]);
  const [tournaments, setTournaments] = React.useState<ITournament[]>([]);
  const [selectedTournament, setSelectedTournament] = React.useState<ITournament | null>(null);

  const columns = useMemo(() => [
    { field: 'teamName', headerName: 'Team Name', width: 130 },
    { field: 'played', headerName: 'Played', width: 60 },
    { field: 'won', headerName: 'Won', width: 60 },
    { field: 'tied', headerName: 'Tied', width: 60 },
    { field: 'lost', headerName: 'Lost', width: 60 },
    { field: 'noResult', headerName: 'No Result', width: 100 },
    { field: 'points', headerName: 'Points', width: 60 },
    { field: 'netMatchRate', headerName: 'NRR', width: 100 },
  ], []);

  const getTournamentPointsTable = async (tournamentId: number) => {
    try {
      const response: AxiosResponse<ICommonApiResponse<IPointsTableResponse>> = await getPointsTable(tournamentId);
      const body:ICommonApiResponse<IPointsTableResponse> = response.data;
      if (body.success) {
        setPointsTable(body.data.pointsTable);
      } else {
        const errorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        console.error('Failed to get points table', errorDetails);
        setAPIErrorMessage(`Failed to get points table with error: ${errorDetails.errorMessage}. Please try again.`);
      }
    } catch (error) {
      console.error('Failed to get points table', error);
      setAPIErrorMessage('Failed to get points table with unknown error. Please try again.');
    }
  };

  const getAllTournamentsNames = async () => {
    try {
      const response: AxiosResponse<ICommonApiResponse<ITournament[]>> = await getAllTournaments();
      const body:ICommonApiResponse<ITournament[]> = response.data;
      if (body.success) {
        const responseTournaments: ITournament[] = body.data;
        const { tournamentId: firstTournamentId } = responseTournaments[0];
        setSelectedTournament(responseTournaments[0]);
        setTournaments(responseTournaments);
        getTournamentPointsTable(firstTournamentId);
      } else {
        const errorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        console.error('Failed to get tournaments', errorDetails);
        setAPIErrorMessage(`Failed to get tournaments with error: ${errorDetails.errorMessage}. Please try again.`);
      }
    } catch (error) {
      console.error('Failed to get tournaments', error);
      setAPIErrorMessage('Failed to get tournaments with unknown error. Please try again.');
    }
  };

  const handleTournamentChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    const currentSelectedTournamentName = event.target.value as string;
    const currentSelectedTournament: ITournament | undefined = tournaments.find((tournament) => tournament.tournamentName === currentSelectedTournamentName);
    setSelectedTournament(currentSelectedTournament!);
    getTournamentPointsTable(currentSelectedTournament!.tournamentId);
  };

  React.useEffect(() => {
    getAllTournamentsNames();
  }, []);

  return (
    <Box
      sx={{
        height: 800,
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
      }}
    >
      <Typography
        variant="h5"
        component="h3"
        sx={{ textAlign: 'center', mt: 3, mb: 3 }}
      >
        {apiErrorMessage}
      </Typography>
      <Typography
        variant="h5"
        component="h3"
        sx={{ textAlign: 'center', mt: 3, mb: 3 }}
      >
        Points Table
      </Typography>
      <>
        <InputLabel>Select tournament name</InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={selectedTournament?.tournamentName || ''}
          label="Select an option"
          onChange={handleTournamentChange}
        >
          {tournaments.map((option) => (
            <MenuItem key={String(option.tournamentId)} value={option.tournamentName}>
              {option.tournamentName}
            </MenuItem>
          ))}
        </Select>
      </>
      {(pointsTable && pointsTable.length > 0) && (
        <DataGrid
          columns={columns}
          rows={pointsTable}
          getRowId={(row) => row.teamName}
        />
      )}
    </Box>
  );
};

export default PointsTable;
