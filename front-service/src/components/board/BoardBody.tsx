import React from 'react';
import styled from "styled-components"
import { Outlet } from 'react-router-dom';

const BoardBodyDiv = styled.div`
    width: 1312px;
    height: 786px;

    position: relative;
    background: white;
    border-radius: 16px;
    box-shadow: 0 0 8px 0 rgba(0, 0, 0, 0.04);

    margin: 0 auto;
    margin-top: 96px;
    margin-bottom: 32px;
    display: flex;
    flex-direction: column;
    overflow: auto;
`
const BoardBody = () => {
    return(
        <BoardBodyDiv>
            <Outlet />
        </BoardBodyDiv>
    );
}

export default BoardBody