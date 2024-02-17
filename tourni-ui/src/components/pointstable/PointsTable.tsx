import React, { useEffect, useMemo, useState } from 'react';
import {
  Box, Typography, MenuItem, Select, InputLabel, FormControl,
} from '@mui/material';

import { DataGrid } from '@mui/x-data-grid';
import { AxiosResponse } from 'axios';
import {
  ICommonApiResponse, IPointsTableResponse, IPointstable, ITournament,
} from '../../types/Types';
import { getPointsTable, getAllTournaments } from '../../services/TournamentService';

const PointsTable: React.FC = () => {
  const [apiErrorMessage, setAPIErrorMessage] = useState<string>('');
  const [pointsTable, setPointsTable] = useState<IPointstable[]>([]);
  const [tournaments, setTournaments] = useState<ITournament[]>([]);
  const [selectedTournamentId, setSelectedTournamentId] = useState<number | null>(null);

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

  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const response: AxiosResponse<ICommonApiResponse<ITournament[]>> = await getAllTournaments();
        const body: ICommonApiResponse<ITournament[]> = response.data;
        if (body.success) {
          const responseTournaments: ITournament[] = body.data;
          if (responseTournaments.length > 0) {
            const { tournamentId } = responseTournaments[0];
            setSelectedTournamentId(tournamentId);
            setTournaments(responseTournaments);
            fetchPointsTable(tournamentId);
          } else {
            setAPIErrorMessage('No tournaments found.');
          }
        } else {
          const errorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'Unknown error' };
          setAPIErrorMessage(`Failed to get tournaments: ${errorDetails.errorMessage}`);
        }
      } catch (error) {
        setAPIErrorMessage('Failed to get tournaments with an unknown error. Please try again later.');
      }
    };

    fetchTournaments();
  }, []);

  const fetchPointsTable = async (tournamentId: number) => {
    try {
      const response: AxiosResponse<ICommonApiResponse<IPointsTableResponse>> = await getPointsTable(tournamentId);
      const body: ICommonApiResponse<IPointsTableResponse> = response.data;
      if (body.success) {
        setPointsTable(body.data.pointsTable);
      } else {
        const errorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'Unknown error' };
        setAPIErrorMessage(`Failed to get points table: ${errorDetails.errorMessage}`);
      }
    } catch (error) {
      setAPIErrorMessage('Failed to get points table with unknown error. Please try again later.');
    }
  };

  const handleTournamentChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    const tournamentId = event.target.value as number;
    setSelectedTournamentId(tournamentId);
    fetchPointsTable(tournamentId);
  };

  return (
    <Box sx={{ height: 800, width: '100%' }}>
      {apiErrorMessage && (
        <Typography variant="h6" color="error" gutterBottom>
          {apiErrorMessage}
        </Typography>
      )}
      <Typography variant="h5" component="h3" sx={{ textAlign: 'center', mt: 3, mb: 3 }}>
        Points Table
      </Typography>
      <FormControl fullWidth margin="normal">
        <InputLabel id="tournament-select-label">Select Tournament</InputLabel>
        <Select
          labelId="tournament-select-label"
          id="tournament-select"
          value={selectedTournamentId || ''}
          label="Select Tournament"
          onChange={handleTournamentChange}
        >
          {tournaments.map((tournament) => (
            <MenuItem key={tournament.tournamentId} value={tournament.tournamentId}>
              {tournament.tournamentName}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      {pointsTable.length > 0 && (
        <DataGrid
          columns={columns}
          rows={pointsTable}
          getRowId={(row) => row.teamId}
          autoHeight
          pageSize={5}
          rowsPerPageOptions={[5, 10, 20]}
        />
      )}
    </Box>
  );
};

export default PointsTable;