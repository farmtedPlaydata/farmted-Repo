import React from "react";
import styled, { createGlobalStyle } from "styled-components"
import Board from "./components/board/Board";
import Login from "./Login";
import { BrowserRouter } from 'react-router-dom';
import Main from "./components/Main";

const GlobalStyle = createGlobalStyle`
    body{
        background: #e9ecef;
        display: flex;
        justify-content: flex-end;
    }
`
const App = () => (
    <BrowserRouter>
        <GlobalStyle/>
<!--         <Login/> -->
        <Main/>
    </BrowserRouter>
);

export default App;