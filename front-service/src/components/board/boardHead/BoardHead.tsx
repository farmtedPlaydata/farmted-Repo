import React from "react"
import styled from "styled-components"

interface Props{
    totalBoard : number;
    category: BoardType;
}
// 게시글 카테고리
enum BoardType{
    AUCTION = "경매",
    COMMISTION = "구매요청",
    SALE = "판매",
    NOTICE = "공지사항",
    CUSTOMER_SERVICE = "고객센터",
    PRODUCT = "상품"
}
const BoardHeadDiv = styled.div`
    padding-top: 48px;
    padding-left: 32px;
    padding-right: 32px;
    padding-bottom: 24px;
    border-bottom: 1px solid #e9ecef;

    h1{
        margin: 0;
        font-size: 36px;
        color: #343a40;
    }
    .day {
        margin-top: 4px;
        color: #868e96;
        font-size: 21px;
    }
    .tasks-left {
        color: #20c997;
        font-size: 18px;
        margin-top: 40px;
        font-weight: bold;
    }
`
const BoardHead = (props:Props) => {
    return (
        <BoardHeadDiv>
            <h1>검색 결과</h1>
            <div className="day">{props.category}</div>
            <div className="tasks-left">{props.totalBoard}개 검색됨</div>
        </BoardHeadDiv>
    )
}

export default BoardHead