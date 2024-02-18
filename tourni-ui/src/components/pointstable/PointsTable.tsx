import React, { useEffect, useMemo, useState } from 'react';
import {
  Box, Typography, MenuItem, Select, InputLabel, FormControl, styled,
} from '@mui/material';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { AxiosResponse } from 'axios';
import {
  ICommonApiResponse, IErrorDetails, IPointsTableResponse, IPointstable, ITournament,
} from '../../types/Types';
import { getPointsTable, getAllTournaments } from '../../services/TournamentService';

const Container = styled(Box)(() => ({
  height: 800,
  width: '100%',
  display: 'flex',
  alignItems: 'center',
  flexDirection: 'column',
  justifyContent: 'center',
}));

const ErrorMessageSection = styled(Typography)(({ theme }) => ({
  variant: 'h6',
  color: 'error',
  marginBottom: theme.spacing(2),
}));

const PointsTableHeader = styled(Typography)(({ theme }) => ({
  textAlign: 'center',
  marginTop: theme.spacing(3),
  marginBottom: theme.spacing(3),
}));

const StyledFormControl = styled(FormControl)(({ theme }) => ({
  display: 'flex',
  marginTop: theme.spacing(3),
  marginBottom: theme.spacing(3),
}));

const StyledDataGrid = styled(DataGrid)(({ theme }) => ({
  '& .MuiDataGrid-root': {
    color: theme.palette.primary.main,
  },
  '& .MuiDataGrid-columnsContainer': {
    backgroundColor: theme.palette.primary.main,
  },
}));

const PointsTable: React.FC = () => {
  const [apiErrorMessage, setAPIErrorMessage] = useState<string>('');
  const [pointsTable, setPointsTable] = useState<IPointstable[]>([]);
  const [tournaments, setTournaments] = useState<ITournament[]>([]);
  const [selectedTournamentId, setSelectedTournamentId] = useState<number | ''>('');

  const columns: GridColDef[] = useMemo(() => [
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
      const response: AxiosResponse<ICommonApiResponse<IPointsTableResponse>> = await getPointsTable(tournamentId);
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
    <Container>
      {apiErrorMessage && (
        <ErrorMessageSection variant="h6" gutterBottom>
          {apiErrorMessage}
        </ErrorMessageSection>
      )}
      <PointsTableHeader>
        Points Table
      </PointsTableHeader>
      <StyledFormControl>
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
      </StyledFormControl>
      {pointsTable && pointsTable.length > 0 && (
        <StyledDataGrid
          columns={columns}
          rows={pointsTable}
          getRowId={(row) => row.teamName}
          autoHeight
        />
      )}
    </Container>
  );
};

export default PointsTable;
