import React from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";

const StyledWriteButton = styled.button`
  background-color: #20c997;
  color: white;
  font-size: 16px;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  margin-bottom: 40px;
  cursor: pointer;
`;

const WriteButton = () => {
  return (
        <Link to={`/boards/write`}  style={{ textDecoration: 'none' }}>
            <StyledWriteButton>글쓰기</StyledWriteButton>
        </Link>
    )
};
export default WriteButton;