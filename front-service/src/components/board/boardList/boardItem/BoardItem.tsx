import React from "react"
import styled, { css } from "styled-components";
import { LocalDateTime, DateTimeFormatter } from "@js-joda/core";

interface Props{
    board:Board;
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

interface Board{
    memberName: string;
    boardUuid: string;
    boardType: BoardType;
    boardTitle: string;
    viewCount: number;
    createAt: string;
    productName?: string;
    productSource?: string;
    productImage?: string;
    productPrice?: number;
    auctionPrice?: number;  // 선택적 프로퍼티
    auctionDeadline?: string;  // 선택적 프로퍼티
    buyerUUID?: string;
}


// LocalDateTime 문자열을 Date 객체로 변환하는 함수
function parseLocalDateTime(localDateTimeString: string): Date {
    // 예시: "2023-01-01T12:34:56"
    const [datePart, timePart] = localDateTimeString.split("T");
    const [year, month, day] = datePart.split("-");
    const [hour, minute, second] = timePart.split(":");
    // month는 0부터 시작하므로 1을 빼줍니다.
    return new Date(parseInt(year, 10), parseInt(month, 10) - 1, parseInt(day, 10), parseInt(hour, 10), parseInt(minute, 10), parseInt(second, 10));
  }

const BoardItemBlockDiv = styled.div<{status:boolean}>`
    display: flex;
    align-items: center;
    padding-top: 12px;
    padding-bottom: 12px;
    &:hover { /* 해당 블록에 마우스가 올라가면 */
        background: #F0F0F0;
    }
    ${ ({status}) =>/* 경매가 종료되었다면 색변환*/
        !status && css`
            background: #ced4da; */
        `
    }
`;
const TextDiv = styled.div`
    flex: 1;
    font-size: 21px;
    color: #495057;
`;

const BoardItem = (props:Props) => {
    const { board } = props;
    // LocalDateTime이 현재 시간보다 이전인지 여부를 판단하여 status 설정
    // + auctionDeadline이 없는 값이라면(경매가 아닌 경우) true 설정
    const auctionStatus = board.auctionDeadline ? new Date() < parseLocalDateTime(board.auctionDeadline) : true;
    console.log("경매상태"+auctionStatus);
    return(
        <BoardItemBlockDiv id={board.boardUuid} status={auctionStatus}>
            <TextDiv>
                <h3>게시글 제목 : {board.boardTitle}</h3>
                <p>상품명 : {board.productName}</p>
                <p>판매자 : {board.memberName}</p>
                <p>카테고리 : {board.boardType}</p>
                <p>조회수 : {board.viewCount}</p>
                <p>생성일 : {board.createAt}</p>
            </TextDiv>
        </BoardItemBlockDiv>
    )
}
export default BoardItem