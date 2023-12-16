import React from "react";
import styled, { createGlobalStyle } from "styled-components"
import Board from "./components/board/Board";
const GlobalStyle = createGlobalStyle`
    body{
        background: #e9ecef;
        display: flex;
        justify-content: flex-end;
    }
`
const App = () => (
    <>  
        <GlobalStyle/>
        <Board/>
    </>
);

export default App;