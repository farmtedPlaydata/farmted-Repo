import React from "react"
import { Link, Route, Routes, BrowserRouter as Router } from 'react-router-dom';
import BoardRouteController from "./board/boardRoute/BoardRouteController"
import UserRouteController from "./user/userRoute/UserRouteController";

const Main = () => {
    return (
        <>
            <BoardRouteController />
        </>
    )
}
export default Main