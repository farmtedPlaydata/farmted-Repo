import React, {useEffect, useState} from "react"
import styled, { css } from "styled-components";
import { Link } from "react-router-dom";
import { LiaStopwatchSolid } from "react-icons/lia";
import { GrView } from "react-icons/gr";
import { BoardType } from "../../boardHead/boardUtil/BoardType";

interface Props{
    board:Board;
}


interface Board{
    memberName: string;
    memberUuid: string;
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

const BoardItemBlockTr = styled.tr`
    display: inline-flex;
    align-items: center;
    padding-top: 12px;
    padding-bottom: 12px;
    border-radius: 8px;
    border-bottom: 1px solid #20c997;
    width: 100%;
    &:hover { /* 해당 블록에 마우스가 올라가면 */
        background: #d7ffea;
    }
`;
const ListHeadTd = styled.td`
  font-size: 16px;
  margin-right: 10px;
  color: #20c997;
  width: 50px;
`;

const ListCategory = styled.div`
  font-size: 14px;
  color: #000000;
`;

const ListTitle = styled.div`
  font-size: 18px;
  color: #000000;
`;

const ListWriter = styled.div`
  font-size: 14px;
  color: #000000;
`;

const ListTime = styled.td`
    font-size: 14px;
    color: #000000;
    text-align: right;
`;

const TextTd = styled.td`
    font-size: 21px;
    color: #495057;
    margin-left: 20px;
    margin-right: 20px;
`;
const FlexContainer = styled.td`
    display: inline-table;
    flex: 1;
    margin-left: 20px;
`;
const RemainingTimeDiv = styled.div`
    display: flex;
    align-items: center;
    font-size: 16px;
    color: #495755;
    margin-left: auto;
`;

const BoardItem = (props:Props) => {
    const { board } = props;
    const productName = board.productName ? <p>상품명: {board.productName}</p> : null;
    const productPrice = board.productPrice ? <p>상품 가격: {board.productPrice}</p> : null;
    const auctionPrice = board.auctionPrice ? <p>경매 가격: {board.auctionPrice}</p> : null;
    const productImage = board.productImage ? 
    <img src={board.productImage} alt="Product" style={{ maxHeight: '100px', width: 'auto' }} /> : null;
  
    // LocalDateTime이 현재 시간보다 이전인지 여부를 판단하여 status 설정
    // + auctionDeadline이 없는 값이라면(경매가 아닌 경우) true 설정
    let auctionStatus = true;
    if(board.auctionDeadline) auctionStatus = new Date() < parseLocalDateTime(board.auctionDeadline);
    const cratedAt = parseLocalDateTime(board.createAt ?? '');

    let timeComponent;

    if (board.auctionDeadline && auctionStatus) {
        // 경매가 진행 중인 경우
        const parsedDateTime = parseLocalDateTime(board.auctionDeadline);
        if (parsedDateTime) {
        const timeDiff = parsedDateTime.getTime() - new Date().getTime();
        const secondsRemaining = Math.floor(timeDiff / 1000);
        timeComponent = (
            <RemainingTimeDiv style={{ color: 'red' }}>
            <LiaStopwatchSolid />
            {`남은 시간: ${secondsRemaining}초`}
            </RemainingTimeDiv>
        );
        }
    } else {
        // 경매가 종료된 경우
        timeComponent = (
        <RemainingTimeDiv>
            <LiaStopwatchSolid />
            경매 종료
        </RemainingTimeDiv>
        );
    }

    // 날짜 계산
    const detailDate = (a: Date): string => {
        const milliSeconds = new Date().getTime() - a.getTime();
        const seconds = milliSeconds / 1000;
        if (seconds < 60) {return `방금 전`;}
        const minutes = seconds / 60;
        if (minutes < 60) {return `${Math.floor(minutes)}분 전`;}
        const hours = minutes / 60;
        if (hours < 24) {return `${Math.floor(hours)}시간 전`;}
        const days = hours / 24;
        if (days < 7) {return `${Math.floor(days)}일 전`;}
        const weeks = days / 7;
        if (weeks < 5) {return `${Math.floor(weeks)}주 전`;}
        const months = days / 30;
        if (months < 12) {return `${Math.floor(months)}개월 전`;}
        const years = days / 365;return `${Math.floor(years)}년 전`;
      };

      const [secondsRemaining, setSecondsRemaining] = useState<number | null>(null);

      useEffect(() => {
        if (board.auctionDeadline && auctionStatus) {
          const timer = setInterval(() => {
            const timeDiff =
              parseLocalDateTime(board.auctionDeadline!).getTime() -
              new Date().getTime();
            const secondsRemaining = Math.floor(timeDiff / 1000);
            setSecondsRemaining(secondsRemaining);
          }, 1000);
    
          return () => clearInterval(timer);
        }
      }, [auctionStatus, board.auctionDeadline]);

    return(
        <Link to={`/boards/${board.boardUuid}`}  style={{ textDecoration: 'none' }}>
            <BoardItemBlockTr id={board.boardUuid}>
                <ListHeadTd>
                    <GrView />
                    <span>{board.viewCount}</span>
                </ListHeadTd>
                {productImage}
                <TextTd>
                    {productName}
                    {productPrice}
                    {auctionPrice}
                </TextTd>
                <FlexContainer>
                    <ListCategory>
                        <span className="category" style={{ color: "#000000" }}>
                            {board.boardType}
                        </span>
                    </ListCategory>
                    <ListTitle>
                        {board.boardTitle}
                    </ListTitle>
                    <ListWriter>
                        <Link to={`/boards/writer/${board.memberUuid}`}>
                            {board.memberName}
                        </Link>
                    </ListWriter>
                </FlexContainer>
                <ListTime>
                    {detailDate(cratedAt)}
                    {timeComponent}
                </ListTime>
            </BoardItemBlockTr>
        </Link>
    )
}
export default BoardItem