import React from 'react';
import styled from "styled-components"
import BoardItem from './boardItem/BoardItem';

interface Props {
    boardList: Board[];
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

// 게시글 카테고리
enum BoardType{
    AUCTION = "경매",
    COMMISTION = "구매요청",
    SALE = "판매",
    NOTICE = "공지사항",
    CUSTOMER_SERVICE = "고객센터",
    PRODUCT = "상품"
}

const BoardListDiv = styled.div`
    flex: 1;
    padding: 20px 32px;
    padding-bottom: 48px;
    overflow-y: auto;
    background: white;
`

const BoardList = (props:Props) => {
    return (
        <BoardListDiv>
            {props.boardList
                .map((board)=>
                    <BoardItem board={board}/>
        )}
        </BoardListDiv>
    )
}

export default BoardList