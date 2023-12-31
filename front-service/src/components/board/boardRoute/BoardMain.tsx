import React from "react"
import Board from "../Board"
import { Route, Routes, useParams } from "react-router-dom"

const BoardMain = () => {
    const { writerUuid } = useParams<{ writerUuid?: string }>();
    console.log('writerUuid:', writerUuid);
    return (
        <Board writerUuid={writerUuid}/>
    )
}
export default BoardMain