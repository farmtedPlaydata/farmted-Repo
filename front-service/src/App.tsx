import React from "react";
import styled, { createGlobalStyle } from "styled-components"
import {BrowserRouter as Router} from 'react-router-dom';
import Main from "./components/Main";
import UserRouteController from "./components/user/userRoute/UserRouteController";
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
        <Main/>
    </Router>
);

export default App;