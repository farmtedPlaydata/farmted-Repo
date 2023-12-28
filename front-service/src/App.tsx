import React from "react";
import styled, { createGlobalStyle } from "styled-components"
import {BrowserRouter as Router} from 'react-router-dom';
import Main from "./components/Main";
import Sidebar from "./components/sidebar/sidebar";

const GlobalStyle = createGlobalStyle`
    body{
        all: unset;
      display: flex;
      width: 100vw;
      justify-content: center;
      flex-direction: column;
    }
`


const App = () => (
    <Router>
        <Sidebar />
        <GlobalStyle/>
        {/*<BiddingList/>*/}
         <Main/>
    </Router>
);

export default App;