import { Grid, Paper, styled, FormControl, Button, TextField, Select } from '@mui/material';

export const StyledGrid = styled(Grid)`
  margin: 20px auto;
  height: 100vh;
  overflow: auto; /* Enable scrolling if needed */
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
  flex: 1; /* Let the text field take up all available space */
`;

export const StyledFormControl = styled(FormControl)`
  flex: 1; /* Let the form control take up all available space */
  margin: 0 5px; /* Add some margin for spacing */
`;

export const StyledSelect = styled(Select)`
  width: 100%; /* Make the select full width */
`;

export const StyledButton = styled(Button)`
  margin-top: 20px;
  width: 100%; /* Make button full width */
`;
