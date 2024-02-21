import {
  MenuItem, InputLabel, Grid, Typography, Avatar, FormHelperText, Button,
} from '@mui/material';
import log from 'loglevel';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { useState, useEffect } from 'react';
import { AxiosResponse } from 'axios';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router';
import { addMatchResult, getAllTeams, getAllTournaments } from '../../services/TournamentService';
import {
  ICommonApiResponse, IErrorDetails, IMatchResult, ITeam, ITournament,
} from '../../types/Types';
import {
  StyledFormControl, StyledTextField, StyledGrid, StyledPaper, StyledSelect, StyledFormRow,
} from './AddMatchResult.styled';

const AddMatchResult = () => {
  const matchResultStatusOptions = [
    { id: 'COMPLETED', name: 'COMPLETED' },
    { id: 'TIED', name: 'TIED' },
    { id: 'NO_RESULT', name: 'NO_RESULT' },
  ];

  const navigate = useNavigate();

  const [tournamentOptions, setTournamentOptions] = useState<ITournament[]>([]);
  const [teamOptions, setTeamOptions] = useState<ITeam[]>([]);
  const [apiErrorMessage, setAPIErrorMessage] = useState<string>('');

  const { register, handleSubmit, formState: { errors } } = useForm<IMatchResult>({
    mode: 'onTouched',
  });

  const fetchTeams = async () => {
    try {
      const response: AxiosResponse<ICommonApiResponse<ITeam[]>> = await getAllTeams();
      const body = response.data;
      if (!body.success) {
        const errorDetails: IErrorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
        log.error(`Error fetching teams data . Error: ${errorDetails.errorMessage}`);
        setAPIErrorMessage('Error fetching teams data . Please try again later.');
        return;
      }
      const teamsData = body.data;
      setTeamOptions(teamsData);
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
        setTournamentOptions(tournamentsData);
        if (tournamentsData.length > 0) {
          await fetchTeams();
        }
      } catch (error) {
        setAPIErrorMessage('Error fetching tournaments. Please try again later.');
        log.error('Error fetching tournaments:', error);
      }
    };

    fetchTournaments();
  }, []);

  const saveMatchResult = async (data: IMatchResult) => {
    const response: AxiosResponse<ICommonApiResponse<string>> = await addMatchResult(data);
    const body: ICommonApiResponse<string> = response.data;
    if (body.success) {
      log.info('Match result saved successfully');
      navigate('/pointsTable');
    } else {
      log.error('Match result save failed', body.errorDetails);
      const errorDetails: IErrorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
      setAPIErrorMessage(`Match result save failed with error: ${errorDetails.errorMessage}. Please try again.`);
    }
  };

  return (
    <StyledGrid
      container
      justifyContent="center"
      alignItems="center"
      style={{ height: '100vh', margin: '20px 0', zIndex: 1000 }}
    >
      <StyledPaper elevation={10}>
        <Grid container justifyContent="center" alignItems="center" direction="column">
          <Avatar>
            <LockOutlinedIcon />
          </Avatar>
          <Typography variant="h5">Add Match Result</Typography>
        </Grid>
        <form onSubmit={handleSubmit(saveMatchResult)} style={{ display: 'flex', flexDirection: 'column' }}>
          <StyledFormRow item xs={12}>
            <StyledTextField
              label="Match Number"
              type="number"
              {...register('matchNumber', { required: true })}
              error={!!errors.matchNumber}
              helperText={errors.matchNumber && 'Match Number is required'}
            />
            <StyledFormControl>
              <InputLabel>Tournament Name</InputLabel>
              <StyledSelect
                label="Select Tournament"
                {...register('tournamentName', { required: true })}
                error={!!errors.tournamentName}
              >
                {tournamentOptions.map((tournament) => (
                  <MenuItem key={tournament.tournamentId} value={tournament.tournamentId}>
                    {tournament.tournamentName}
                  </MenuItem>
                ))}
              </StyledSelect>
              {!!errors.tournamentName && <FormHelperText sx={{ color: 'red' }}>Tournament name is required</FormHelperText>}
            </StyledFormControl>
          </StyledFormRow>
          <StyledFormRow item xs={12}>
            <StyledFormControl>
              <InputLabel>Winner Team ID</InputLabel>
              <StyledSelect
                label="Select Winner Team"
                {...register('winnerTeamName', { required: true })}
                error={!!errors.winnerTeamName}
              >
                {teamOptions.map((team) => (
                  <MenuItem key={team.teamId} value={team.teamId}>
                    {team.teamName}
                  </MenuItem>
                ))}
              </StyledSelect>
              {!!errors.winnerTeamName && <FormHelperText sx={{ color: 'red' }}>Winner team name is required</FormHelperText>}
            </StyledFormControl>
            <StyledFormControl>
              <InputLabel>Loser Team ID</InputLabel>
              <StyledSelect
                label="Select Loser Team"
                {...register('loserTeamName', { required: true })}
                error={!!errors.loserTeamName}
              >
                {teamOptions.map((team) => (
                  <MenuItem key={team.teamId} value={team.teamId}>
                    {team.teamName}
                  </MenuItem>
                ))}
              </StyledSelect>
              {!!errors.loserTeamName && <FormHelperText sx={{ color: 'red' }}>Loser team name is required</FormHelperText>}
            </StyledFormControl>
          </StyledFormRow>
          <StyledFormRow item xs={12}>
            <StyledFormControl>
              <InputLabel>Team One ID</InputLabel>
              <StyledSelect
                label="Select Team One"
                {...register('teamOneName', { required: true })}
                error={!!errors.teamOneName}
              >
                {teamOptions.map((team) => (
                  <MenuItem key={team.teamId} value={team.teamId}>
                    {team.teamName}
                  </MenuItem>
                ))}
              </StyledSelect>
              {!!errors.teamOneName && <FormHelperText sx={{ color: 'red' }}>Team One name is required</FormHelperText>}
            </StyledFormControl>
            <StyledFormControl>
              <InputLabel>Team Two ID</InputLabel>
              <StyledSelect
                label="Select Team Two"
                {...register('teamTwoName', { required: true })}
                error={!!errors.teamTwoName}
              >
                {teamOptions.map((team) => (
                  <MenuItem key={team.teamId} value={team.teamId}>
                    {team.teamName}
                  </MenuItem>
                ))}
              </StyledSelect>
              {!!errors.teamTwoName && <FormHelperText sx={{ color: 'red' }}>Team Two name is required</FormHelperText>}
            </StyledFormControl>
          </StyledFormRow>
          <StyledFormRow item xs={12}>
            <StyledTextField
              label="Team One Score"
              type="number"
              defaultValue={151}
              {...register('teamOneScore', { required: true })}
              error={!!errors.teamOneScore}
              helperText={errors.teamOneScore && 'Team One Score is required'}
            />
            <StyledTextField
              label="Team Two Score"
              type="number"
              defaultValue={152}
              {...register('teamTwoScore', { required: true })}
              error={!!errors.teamTwoScore}
              helperText={errors.teamTwoScore && 'Team Two Score is required'}
            />
          </StyledFormRow>
          <StyledFormRow item xs={12}>
            <StyledTextField
              label="Team One Wickets"
              type="number"
              defaultValue={4}
              {...register('teamOneWickets', { required: true })}
              error={!!errors.teamOneWickets}
              helperText={errors.teamOneWickets && 'Team One Wickets is required'}
            />
            <StyledTextField
              label="Team Two Wickets"
              type="number"
              defaultValue={2}
              {...register('teamTwoWickets', { required: true })}
              error={!!errors.teamTwoWickets}
              helperText={errors.teamTwoWickets && 'Team Two Wickets is required'}
            />
          </StyledFormRow>
          <StyledFormRow item xs={12}>
            <StyledTextField
              label="Team One Overs Played"
              type="number"
              defaultValue={20}
              {...register('teamOneOversPlayed', { required: true })}
              error={!!errors.teamOneOversPlayed}
              helperText={errors.teamOneOversPlayed && 'Team One Overs Played is required'}
            />
            <StyledTextField
              label="Team Two Overs Played"
              type="number"
              defaultValue={17.5}
              {...register('teamTwoOversPlayed', { required: true })}
              error={!!errors.teamTwoOversPlayed}
              helperText={errors.teamTwoOversPlayed && 'Team Two Overs Played is required'}
            />
          </StyledFormRow>
          <StyledFormRow item xs={12}>
            <StyledFormControl>
              <InputLabel>Match Result Status</InputLabel>
              <StyledSelect
                label="Select Match Result Status"
                {...register('matchResultStatus', { required: true })}
                error={!!errors.matchResultStatus}
              >
                {matchResultStatusOptions.map((status) => (
                  <MenuItem key={status.id} value={status.id}>
                    {status.name}
                  </MenuItem>
                ))}
              </StyledSelect>
              {!!errors.matchResultStatus && <FormHelperText sx={{ color: 'red' }}>Match Result Status is required</FormHelperText>}
            </StyledFormControl>
          </StyledFormRow>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            style={{
              marginTop: '20px',
            }}
          >
            Save Match Result
          </Button>
          <Typography style={{ margin: 7, color: 'red' }} variant="body1">
            {apiErrorMessage}
          </Typography>
        </form>
      </StyledPaper>
    </StyledGrid>
  );
};

export default AddMatchResult;
