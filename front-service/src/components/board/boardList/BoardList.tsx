import React from 'react';
import styled from "styled-components"
import BoardItem from './boardItem/BoardItem';
import { BoardType } from '../boardHead/boardUtil/BoardType';

interface Props {
    boardList: Board[];
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


const BoardListTable = styled.table`
    width: 100%;
    border-collapse: collapse;
`

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
            <BoardListTable>
                <tbody>
                    {props.boardList.map((board) => (
                        <BoardItem key={board.boardUuid} board={board} />
                    ))}
                </tbody>
            </BoardListTable>
        </BoardListDiv>
    )
}

export default BoardList