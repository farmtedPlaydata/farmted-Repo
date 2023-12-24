import React from "react";
import styled, { createGlobalStyle } from "styled-components"
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
        <Main/>
    </BrowserRouter>
);

export default App;