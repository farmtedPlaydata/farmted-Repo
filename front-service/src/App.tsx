// App.tsx
import React from 'react';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Typography from '@mui/material/Typography'; // 수정된 부분
import Login from './Login';

// 녹색 테마 생성
const theme = createTheme({
    palette: {
        primary: {
            main: '#4CAF50', // 녹색 색상 코드
        },
    },
});

const App = () => {
    return (
        <ThemeProvider theme={theme}>
            <div>
                <Typography variant="h3" color="primary" fontWeight="bold">
                    팜티드
                </Typography>
                <Login />
            </div>
        </ThemeProvider>
    );
};

export default App;