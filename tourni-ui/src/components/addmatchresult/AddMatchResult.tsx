import {
  MenuItem, InputLabel,
} from '@mui/material';
import { AddBox } from '@mui/icons-material';
import log from 'loglevel';
import { useState, useEffect } from 'react';
import { AxiosResponse } from 'axios';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router';
import { SelectChangeEvent } from '@mui/material/Select';
import { addMatchResult, getAllTeams, getAllTournaments } from '../../services/TournamentService';
import {
  ICommonApiResponse, IErrorDetails, IMatchResult, ITeam, ITournament,
} from '../../types/Types';
import {
  StyledFormControl, StyledTextField, StyledGrid, StyledPaper, StyledSelect, StyledFormRow,
  HeaderGrid, StyledAvatar, StyledForm, StyledFormHelperText, HeaderText, ButtonBox, ErrorMessageSection, StyledButton,
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

  const fetchTeams = async (tournamentId: number) => {
    try {
      const response: AxiosResponse<ICommonApiResponse<ITeam[]>> = await getAllTeams(tournamentId);
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
      } catch (error) {
        setAPIErrorMessage('Error fetching tournaments. Please try again later.');
        log.error('Error fetching tournaments:', error);
      }
    };

    fetchTournaments();
  }, []);

    const onTournamentChange = (event: SelectChangeEvent<unknown>, _child?: React.ReactNode) => {
      const tournamentId = Number(event.target.value);
      fetchTeams(tournamentId);
    };

  const saveMatchResult = async (data: IMatchResult) => {
    const response: AxiosResponse<ICommonApiResponse<string>> = await addMatchResult(data);
    const body: ICommonApiResponse<string> = response.data;
    if (body.success) {
      log.info('Match result saved successfully');
      navigate('/pointsTable');
    } else {
      log.error('Match result save failed', JSON.stringify(body.errorDetails));
      const errorDetails: IErrorDetails = body.errorDetails || { errorCode: 0, errorMessage: 'unknown error' };
      setAPIErrorMessage(`Match result save failed with error: ${errorDetails.errorMessage}. Please try again.`);
    }
  };

  return (
    <StyledGrid
      container
    >
      <StyledPaper elevation={10}>
        <HeaderGrid container>
          <StyledAvatar>
            <AddBox />
          </StyledAvatar>
          <HeaderText variant="h5">Add Match Result</HeaderText>
        </HeaderGrid>
        <StyledForm onSubmit={handleSubmit(saveMatchResult)}>
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
                onChange={onTournamentChange}
              >
                {tournamentOptions.map((tournament) => (
                  <MenuItem key={tournament.tournamentId} value={tournament.tournamentId}>
                    {tournament.tournamentName}
                  </MenuItem>
                ))}
              </StyledSelect>
              {!!errors.tournamentName && <StyledFormHelperText>Tournament name is required</StyledFormHelperText>}
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
              {!!errors.winnerTeamName && <StyledFormHelperText>Winner team name is required</StyledFormHelperText>}
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
              {!!errors.loserTeamName && <StyledFormHelperText>Loser team name is required</StyledFormHelperText>}
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
              {!!errors.teamOneName && <StyledFormHelperText>Team One name is required</StyledFormHelperText>}
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
              {!!errors.teamTwoName && <StyledFormHelperText>Team Two name is required</StyledFormHelperText>}
            </StyledFormControl>
          </StyledFormRow>
          <StyledFormRow item xs={12}>
            <StyledTextField
              label="Team One Score"
              type="number"
              {...register('teamOneScore', { required: true })}
              error={!!errors.teamOneScore}
              helperText={errors.teamOneScore && 'Team One Score is required'}
            />
            <StyledTextField
              label="Team Two Score"
              type="number"
              {...register('teamTwoScore', { required: true })}
              error={!!errors.teamTwoScore}
              helperText={errors.teamTwoScore && 'Team Two Score is required'}
            />
          </StyledFormRow>
          <StyledFormRow item xs={12}>
            <StyledTextField
              label="Team One Wickets"
              type="number"
              {...register('teamOneWickets', { required: true })}
              error={!!errors.teamOneWickets}
              helperText={errors.teamOneWickets && 'Team One Wickets is required'}
            />
            <StyledTextField
              label="Team Two Wickets"
              type="number"
              {...register('teamTwoWickets', { required: true })}
              error={!!errors.teamTwoWickets}
              helperText={errors.teamTwoWickets && 'Team Two Wickets is required'}
            />
          </StyledFormRow>
          <StyledFormRow item xs={12}>
            <StyledTextField
              label="Team One Overs Played"
              type="number"
              {...register('teamOneOversPlayed', { required: true })}
              error={!!errors.teamOneOversPlayed}
              helperText={errors.teamOneOversPlayed && 'Team One Overs Played is required'}
            />
            <StyledTextField
              label="Team Two Overs Played"
              type="number"
              inputProps={{ step: '0.1', pattern: '\\d+(\\.\\d{1})?' }} // Specify step and pattern attributes
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
              {!!errors.matchResultStatus && <StyledFormHelperText>Match Result Status is required</StyledFormHelperText>}
            </StyledFormControl>
          </StyledFormRow>
          <ButtonBox>
            <StyledButton
              type="submit"
              variant="contained"
              color="primary"
            >
              Save Match Result
            </StyledButton>
          </ButtonBox>
          <ErrorMessageSection>
            {apiErrorMessage}
          </ErrorMessageSection>
        </StyledForm>
      </StyledPaper>
    </StyledGrid>
  );
};

export default AddMatchResult;
