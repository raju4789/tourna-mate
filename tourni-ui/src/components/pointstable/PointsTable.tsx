import React, { useEffect, useMemo, useState } from 'react';
import {
  Box, Typography, MenuItem, Select, InputLabel, FormControl,
} from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import { AxiosResponse } from 'axios';
import {
  ICommonApiResponse, IErrorDetails, IPointsTableResponse, IPointstable, ITournament,
} from '../../types/Types';
import { getPointsTable, getAllTournaments } from '../../services/TournamentService';

const PointsTable: React.FC = () => {
  const [apiErrorMessage, setAPIErrorMessage] = useState<string>('');
  const [pointsTable, setPointsTable] = useState<IPointstable[]>([]);
  const [tournaments, setTournaments] = useState<ITournament[]>([]);
  const [selectedTournamentId, setSelectedTournamentId] = useState<number | ''>('');

  const columns = useMemo(() => [
    { field: 'teamName', headerName: 'Team Name', width: 250 },
    { field: 'played', headerName: 'Played', width: 60 },
    { field: 'won', headerName: 'Won', width: 60 },
    { field: 'tied', headerName: 'Tied', width: 60 },
    { field: 'lost', headerName: 'Lost', width: 60 },
    { field: 'noResult', headerName: 'No Result', width: 100 },
    { field: 'points', headerName: 'Points', width: 60 },
    { field: 'netMatchRate', headerName: 'NRR', width: 100 },
  ], []);

  const fetchPointsTable = async (tournamentId: number) => {
    try {
      const response:AxiosResponse<ICommonApiResponse<IPointsTableResponse>> = await getPointsTable(tournamentId);
      const body = response.data;
      if (!body.success) {
        const errorDetails: IErrorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        console.error(`Error fetching points table data for tournament: ${tournamentId}. Error: ${errorDetails.errorMessage}`);
        setAPIErrorMessage(`Error fetching points table data for tournament: ${tournamentId}. Please try again later.`);
        return;
      }
      const pointsTableData = body.data.pointsTable;
      setPointsTable(pointsTableData);
    } catch (error) {
      setAPIErrorMessage('Error fetching points table data. Please try again later.');
      console.error('Error fetching points table data:', error);
    }
  };

  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const response = await getAllTournaments();
        if (!response.data.success) {
          const errorDetails: IErrorDetails = response.data.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
          console.error(`Error fetching tournaments: ${errorDetails.errorMessage}`);
          setAPIErrorMessage('Error fetching tournaments. Please try again later.');
          return;
        }
        const tournamentsData = response.data.data;
        setTournaments(tournamentsData);
        if (tournamentsData.length > 0) {
          const firstTournamentId = tournamentsData[0].tournamentId;
          setSelectedTournamentId(firstTournamentId);
          await fetchPointsTable(firstTournamentId);
        }
      } catch (error) {
        setAPIErrorMessage('Error fetching tournaments. Please try again later.');
        console.error('Error fetching tournaments:', error);
      }
    };

    fetchTournaments();
  }, []);

  // Event handler for tournament selection change
  const handleTournamentChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    const tournamentId = event.target.value as number;
    setSelectedTournamentId(tournamentId);
    fetchPointsTable(tournamentId);
  };

  return (
    <Box sx={{
      height: 800, width: '100%', display: 'flex', alignItems: 'center', flexDirection: 'column', justifyContent: 'center',
    }}
    >
      {apiErrorMessage && (
        <Typography variant="h6" color="error" gutterBottom>
          {apiErrorMessage}
        </Typography>
      )}
      <Typography variant="h5" component="h3" sx={{ textAlign: 'center', mt: 3, mb: 3 }}>
        Points Table
      </Typography>
      <FormControl sx={{ display: 'flex', mt: 3, mb: 3 }}>
        <InputLabel>Tournament</InputLabel>
        <Select
          value={selectedTournamentId}
          label="Select Tournament"
          onChange={handleTournamentChange}
        >
          {tournaments.map((tournament: ITournament) => (
            <MenuItem key={tournament.tournamentId} value={tournament.tournamentId}>
              {tournament.tournamentName}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      {pointsTable && pointsTable.length > 0 && (
        <DataGrid
          columns={columns}
          rows={pointsTable}
          getRowId={(row) => row.teamName}
          autoHeight
        />
      )}
    </Box>
  );
};

export default PointsTable;
