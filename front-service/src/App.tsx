import React from "react";
import styled, { createGlobalStyle } from "styled-components"
import Board from "./components/board/Board";
import Login from "./Login";
const GlobalStyle = createGlobalStyle`
    body{
        background: #e9ecef;
        display: flex;
        justify-content: flex-end;
    }
`
const App = () => (
    <>
        <Login/>
        {/*<GlobalStyle/>*/}
        {/*<Board/>*/}

    </>
);

export default App;