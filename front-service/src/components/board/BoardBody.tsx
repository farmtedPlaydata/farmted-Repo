import React, { createContext, useState } from 'react';
import styled from "styled-components"
import BoardList from "./boardList/BoardList";

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
`
const BoardBody = (props:{children: React.ReactNode}) => {
    return(
        <BoardBodyDiv>{props.children}</BoardBodyDiv>
    );
}

export default BoardBody