import React, { useEffect, useMemo, useState } from 'react';
import { Box, Typography, MenuItem, Select, InputLabel, FormControl } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import { IPointsTableResponse, IPointstable, ITournament } from '../../types/Types';
import { getPointsTable, getAllTournaments } from '../../services/TournamentService';

const PointsTable: React.FC = () => {
  const [apiErrorMessage, setAPIErrorMessage] = useState<string>('');
  const [pointsTable, setPointsTable] = useState<IPointstable[]>([]);
  const [tournaments, setTournaments] = useState<ITournament[]>([]);
  const [selectedTournamentId, setSelectedTournamentId] = useState<number | ''>('');

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

  // Effect for fetching all tournaments on mount
  useEffect(() => {
    getAllTournaments().then(response => {
      const tournamentsData = response.data.data; // This path might need to be adjusted based on your actual API response
      setTournaments(tournamentsData);
      if (tournamentsData.length > 0) {
        const firstTournamentId = tournamentsData[0].tournamentId;
        setSelectedTournamentId(firstTournamentId);
        fetchPointsTable(firstTournamentId);
      }
    }).catch(error => {
      setAPIErrorMessage('Error fetching tournaments. Please try again later.');
      console.error('Error fetching tournaments:', error);
    });
  }, []);

  // Function for fetching points table for a given tournament
  const fetchPointsTable = (tournamentId: number) => {
    getPointsTable(tournamentId).then(response => {
      const pointsTableData = response.data.data.pointsTable; // This path might need to be adjusted based on your actual API response
      setPointsTable(pointsTableData);
    }).catch(error => {
      setAPIErrorMessage('Error fetching points table data. Please try again later.');
      console.error('Error fetching points table data:', error);
    });
  };

  // Event handler for tournament selection change
  const handleTournamentChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    const tournamentId = event.target.value as number;
    setSelectedTournamentId(tournamentId);
    setPointsTable([]); // Clear points table before fetching new one
    fetchPointsTable(tournamentId);
  };

  // PointsTable component layout
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