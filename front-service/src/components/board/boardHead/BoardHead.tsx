import React from "react"
import styled from "styled-components"
import CategoryList from "./boardUtil/CategoryList";
import SearchBar from "./boardUtil/SearchBar";
import WriteButton from "./boardUtil/WriteButton";
import { BoardType } from "./boardUtil/BoardType";
import {Button} from "react-bootstrap";

interface Props{
    totalBoard? : number;
    category: BoardType;
    onCategoryChange: (newCategory: BoardType) => void;
}
const BoardHeadDiv = styled.div`
    padding-top: 48px;
    padding-left: 32px;
    padding-right: 32px;
    padding-bottom: 24px;
    border-bottom: 1px solid #20c997;
    display: flex;
    justify-content: space-between;
    align-items: center;
`;
const HeadInfoDiv = styled.div`
    h1 {
        margin: 0;
        font-size: 36px;
        color: #343a40;
    }
    .category {
        margin-top: 4px;
        color: #868e96;
        font-size: 21px;
    }
    .result-size {
        color: #20c997;
        font-size: 18px;
        margin-top: 40px;
        font-weight: bold;
    }
`
const SearchContainer = styled.div`
    display: flex;
    flex-direction: column; /* 추가: 수직 정렬 */
    align-items: flex-end; /* 추가: 우측 정렬 */
    margin-top: 20px;
`;

const BoardHead = (props:Props) => {
    const { category, totalBoard = 0, onCategoryChange } = props;
    return (
        <BoardHeadDiv>
            <HeadInfoDiv>
                <h1>검색 결과</h1>
                <div className="category">{category}</div>
                <div className="result-size">{totalBoard}개 검색됨</div>
            </HeadInfoDiv>
            <SearchContainer>
                <WriteButton/>
                <SearchBar onSearch={(query) => console.log("Searching with query:", query)} />
                <CategoryList categories={category} onCategoryChange={onCategoryChange} />
            </SearchContainer>
        </BoardHeadDiv>
    )
}

export default BoardHead