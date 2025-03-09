import {
  StyledAppBar, StyledToolbar, StyledTypography, StyledButton,
} from './Footer.styled';

const Footer: React.FC = () => (
  <StyledAppBar position="static" color="default" elevation={0}>
    <StyledToolbar>
      <StyledTypography variant="h6" color="inherit" noWrap>
        Copyright © Raju MLN.
      </StyledTypography>

      <StyledButton href="https://www.linkedin.com/in/raju-m-l-n/" variant="outlined" color="primary">
        Linkedin
      </StyledButton>
    </StyledToolbar>
  </StyledAppBar>
);

export default Footer;
