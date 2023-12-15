// Import necessary components and icons
import { Button, Container, CssBaseline, Typography } from '@mui/material';
import { styled } from '@mui/system';
import GoogleIcon from './icons/google-icon.svg';
import KakaoIcon from './icons/kakao-icon.svg';

// ... (existing imports)

const StyledGoogleButton = styled(Button)({
    backgroundColor: '#4285F4', // Google blue color
    color: '#ffffff', // White text color
    margin: theme => theme.spacing(1, 0), // Add margin (adjust as needed)
    '&:hover': {
        backgroundColor: '#357ae8', // Google blue color on hover
    },
});

const StyledKakaoButton = styled(Button)({
    backgroundColor: '#FFEB00', // Kakao Talk yellow color
    color: '#000000', // Black text color
    margin: theme => theme.spacing(1, 0), // Add margin (adjust as needed)
    '&:hover': {
        backgroundColor: '#FFC300', // Kakao Talk yellow color on hover
    },
});

const Login = () => {
    // ... (existing code)

    return (
        <StyledContainer component="main" maxWidth="xs">
            <CssBaseline />
            <div>
                {/* ... (existing code) */}
                <StyledButton
                    type="button"
                    fullWidth
                    variant="contained"
                    color="primary"
                    onClick={handleLogin}
                >
                    로그인
                </StyledButton>
                <StyledCustomDivider />
                <StyledButton
                    type="button"
                    fullWidth
                    variant="contained"
                    color="secondary"
                    onClick={handleSignUp}
                >
                    회원가입
                </StyledButton>
                <StyledGoogleButton
                    type="button"
                    fullWidth
                    variant="contained"
                    startIcon={<GoogleIcon />} // Replace with the actual Google icon
                    onClick={handleGoogleLogin}
                >
                    Google로 계속
                </StyledGoogleButton>
                <StyledKakaoButton
                    type="button"
                    fullWidth
                    variant="contained"
                    startIcon={<KakaoIcon />} // Replace with the actual Kakao Talk icon
                    onClick={handleKakaoLogin}
                >
                    Kakao Talk로 계속
                </StyledKakaoButton>
                <StyledButton
                    type="button"
                    fullWidth
                    variant="contained"
                    color="primary"
                    onClick={handleLogin}
                >
                    <img src={GoogleIcon} alt="Google 로그인" /> Google로 계속
                </StyledButton>

                <StyledButton
                    type="button"
                    fullWidth
                    variant="contained"
                    color="secondary"
                    onClick={handleSignup}
                >
                    <img src={KakaoIcon} alt="Kakao 로그인" /> Kakao Talk로 계속
                </StyledButton>
            </div>
        </StyledContainer>
    );
};

export default Login;

