import React, { useEffect, useMemo, useState } from 'react';
import {
  MenuItem, InputLabel, ThemeProvider,
} from '@mui/material';
import { GridColDef } from '@mui/x-data-grid';
import { AxiosResponse } from 'axios';
import log from 'loglevel';
import {
  ICommonApiResponse, IErrorDetails, IPointsTableResponse, IPointstable, ITournament,
} from '../../types/Types';
import { getPointsTable, getAllTournaments } from '../../services/TournamentService';
import {
  Container, ErrorMessageSection, PointsTableHeader, StyledDataGrid, StyledFormControl, StyledSelect, theme,
} from './PointsTable.styled';

const PointsTable: React.FC = () => {
  const [apiErrorMessage, setAPIErrorMessage] = useState<string>('');
  const [pointsTable, setPointsTable] = useState<IPointstable[]>([]);
  const [tournaments, setTournaments] = useState<ITournament[]>([]);
  const [selectedTournamentId, setSelectedTournamentId] = useState<number | ''>('');

  const columns: GridColDef[] = useMemo(() => [
    { field: 'teamName', headerName: 'Team Name', width: 250 },
    { field: 'played', headerName: 'Played', width: 80 },
    { field: 'won', headerName: 'Won', width: 60 },
    { field: 'tied', headerName: 'Tied', width: 60 },
    { field: 'lost', headerName: 'Lost', width: 60 },
    { field: 'noResult', headerName: 'No Result', width: 100 },
    { field: 'points', headerName: 'Points', width: 80 },
    { field: 'netMatchRate', headerName: 'NRR', width: 100 },
  ], []);

  const fetchPointsTable = async (tournamentId: number) => {
    try {
      const response: AxiosResponse<ICommonApiResponse<IPointsTableResponse>> = await getPointsTable(tournamentId);
      const body = response.data;
      if (!body.success) {
        const errorDetails: IErrorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        log.error(`Error fetching points table data for tournament: ${tournamentId}. Error: ${errorDetails.errorMessage}`);
        setAPIErrorMessage(`Error fetching points table data for tournament: ${tournamentId}. Please try again later.`);
        return;
      }
      const pointsTableData = body.data.pointsTable;
      setPointsTable(pointsTableData);
    } catch (error) {
      setAPIErrorMessage('Error fetching points table data. Please try again later.');
      log.error('Error fetching points table data:', error);
    }
  };

  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const response = await getAllTournaments();
        if (!response.data.success) {
          const errorDetails: IErrorDetails = response.data.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
          log.error(`Error fetching tournaments: ${errorDetails.errorMessage}`);
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
        log.error('Error fetching tournaments:', error);
      }
    };

    fetchTournaments();
  }, []);

  const handleTournamentChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    const tournamentId = event.target.value as number;
    setSelectedTournamentId(tournamentId);
    fetchPointsTable(tournamentId);
  };

  return (
    <ThemeProvider theme={theme}>
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
          <StyledSelect
            value={selectedTournamentId}
            label="Select Tournament"
            onChange={handleTournamentChange}
            sx={{ width: '200px' }}
          >
            {tournaments.map((tournament: ITournament) => (
              <MenuItem key={tournament.tournamentId} value={tournament.tournamentId}>
                {tournament.tournamentName}
              </MenuItem>
            ))}
          </StyledSelect>
        </StyledFormControl>
        {pointsTable && pointsTable.length > 0 && (
          <StyledDataGrid
            columns={columns}
            rows={pointsTable}
            getRowId={(row) => row.teamName}
            hideFooter
            hideFooterPagination
            autoHeight
          />
        )}
      </Container>
    </ThemeProvider>
  );
};

export default PointsTable;
