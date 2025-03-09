import {
  Grid, Paper, styled, FormControl, TextField, Select, Avatar, FormHelperText, Typography, Box, Button,
} from '@mui/material';

const THEME_COLOUR = '#1976d2';

export const StyledGrid = styled(Grid)`
    justify-content: 'center';
    align-items: 'center';
    height: '100vh';
    margin: '20px 0'; 
`;

export const StyledPaper = styled(Paper)`
    margin: 20px auto;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
`;

export const StyledFormContainer = styled(Grid)`
     width: 100%;
`;

export const StyledFormRow = styled(Grid)`
    display: flex;
    justify-content: space-between;
    gap: 20px;
     margin: 10px 0;
`;

export const StyledTextField = styled(TextField)`
    flex: 1;
`;

export const StyledFormControl = styled(FormControl)`
    flex: 1; 
    margin: 0 5px; 
`;

export const StyledSelect = styled(Select)`
    width: 100%; 
`;

export const HeaderGrid = styled(Grid)`
    display:flex;
    justify-content:center;
    align-items:center;
    flex-direction:row;
    margin-bottom:2%;
`;

export const StyledAvatar = styled(Avatar)`
    background-color: ${THEME_COLOUR};
    margin-bottom: 2%;
    margin-right: 2%;
`;

export const StyledForm = styled('form')`
    display: flex;
    flex-direction: column;
`;

export const StyledFormHelperText = styled(FormHelperText)`
    color: red;
`;

export const HeaderText = styled(Typography)`
    font-size: 2.0rem;
    margin-bottom: 2%;

`;

export const ButtonBox = styled(Box)`
    display:flex;
    justify-content:center;
    align-items:center;
    flex-direction:row;
    margin-bottom:2%;
    width:100%;
`;

export const ErrorMessageSection = styled(Typography)(({ theme }) => ({
  variant: 'h6',
  color: 'error',
  marginBottom: theme.spacing(2),
}));

export const StyledButton = styled(Button)`
    width: 40%;
    margin-top: 3%;
`;
