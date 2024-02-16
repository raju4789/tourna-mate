import React, { useMemo } from 'react';
import { Box, Typography } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import { AxiosResponse } from 'axios';
import { ICommonApiResponse, IPoinstableResponse } from '../../types/Types';
import getPointsTable from '../../services/TournamentService';

const PointsTable: React.FC = () => {
  const [apiErrorMessage, setAPIErrorMessage] = React.useState<string>('');
  const [pointsTable, setPointsTable] = React.useState<IPoinstableResponse[]>([]);

  const columns = useMemo(() => [
    { field: 'teamName', headerName: 'Team Name', width: 130 },
    { field: 'played', headerName: 'Played', width: 60 },
    { field: 'won', headerName: 'Won', width: 60 },
    { field: 'drawn', headerName: 'Drawn', width: 60 },
    { field: 'lost', headerName: 'Lost', width: 60 },
    { field: 'points', headerName: 'Points', width: 60 },
    { field: 'netMatchRate', headerName: 'NRR', width: 100 },
  ], []);

  const getTournamentPointsTable = async (tournamentId: number) => {
    try {
      const response: AxiosResponse<ICommonApiResponse<IPoinstableResponse[]>> = await getPointsTable(tournamentId);
      const body:ICommonApiResponse<IPoinstableResponse[]> = response.data;
      if (body.success) {
        setPointsTable(body.data);
      } else {
        const errorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        setAPIErrorMessage(`Failed to get points table with error: ${errorDetails.errorMessage}. Please try again.`);
      }
    } catch (error) {
      console.error('Failed to get points table', error);
      setAPIErrorMessage('Failed to get points table with unknown error. Please try again.');
    }
  };

  React.useEffect(() => {
    getTournamentPointsTable(101);
  }, []);
  return (
    <Box
      sx={{
        height: 400,
        width: '100%',
      }}
    >
      <Typography
        variant="h5"
        component="h3"
        sx={{ textAlign: 'center', mt: 3, mb: 3 }}
      >
        Points Table
      </Typography>
      <DataGrid
        columns={columns}
        rows={pointsTable}
      />
    </Box>
  );
};

export default PointsTable;
