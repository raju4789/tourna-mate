import {
  Box, Typography, FormControl, styled, createTheme, Select,
} from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';

const THEME_COLOUR = '#1976d2';

export const Container = styled(Box)(() => ({
  height: 'auto',
  width: '100%',
  display: 'flex',
  alignItems: 'center',
  flexDirection: 'column',
  justifyContent: 'center',
}));

export const ErrorMessageSection = styled(Typography)(({ theme }) => ({
  variant: 'h6',
  color: 'error',
  marginBottom: theme.spacing(2),
}));

export const PointsTableHeader = styled(Typography)(({ theme }) => ({
  textAlign: 'center',
  marginTop: theme.spacing(3),
  marginBottom: theme.spacing(3),
  fontSize: '2.0rem',
  fontWeight: 'bold',
  color: THEME_COLOUR,
}));

export const StyledFormControl = styled(FormControl)(({ theme }) => ({
  display: 'flex',
  marginTop: theme.spacing(3),
  marginBottom: theme.spacing(3),
}));

export const theme = createTheme({
  palette: {
    primary: { main: '#556cd6' }, // Example main color
    secondary: { main: '#19857b' }, // Example secondary color
    error: { main: '#ff0000' },
    background: {
      default: '#f0f0f0',
      paper: '#ffffff',
    },
    text: {
      primary: '#2d2d2d',
      secondary: '#555555',
    },
  },
  typography: {
    fontFamily: [
      '"Segoe UI"',
      '"Roboto"',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
    ].join(','),
  },
});

export const StyledDataGrid = styled(DataGrid)(({ theme }) => ({
  '& .MuiDataGrid-root': {
    border: 'none',
  },
  '& .MuiDataGrid-columnHeaderTitle': {
    fontWeight: 600,
  },
  '& .MuiDataGrid-columnHeaders': {
    backgroundColor: THEME_COLOUR,
    color: '#fff',
    fontSize: '1rem',
  },
  '& .MuiDataGrid-columnHeader, & .MuiDataGrid-cell': {
    borderRight: `1px solid ${theme.palette.divider}`,
  },
  '& .MuiDataGrid-row': {
    '&:nth-of-type(even)': {
      backgroundColor: '#1976d22D',
    },
  },
  '& .MuiDataGrid-cell': {
    color: theme.palette.text.secondary,
    fontWeight: 400,
    fontSize: '0.875rem',
    lineHeight: 1.6,
  },
  '& .MuiDataGrid-cell:focus': {
    outline: 'none',
  },
  '& .MuiDataGrid-row:hover': {
    backgroundColor: theme.palette.action.hover,
  },
  '& .MuiDataGrid-footerContainer': {
    borderTop: `1px solid ${theme.palette.divider}`,
  },
}));

export const StyledSelect = styled(Select)(({ theme }) => ({
  backgroundColor: theme.palette.background.paper,
  color: theme.palette.text.primary,
  borderRadius: 4,
  '&:focus': {
    backgroundColor: theme.palette.background.paper,
  },
}));

export const PointsTableSection = styled(Box)`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    gap: 10px;
`;