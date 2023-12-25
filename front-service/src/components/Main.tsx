import React from "react"
import { Link, Route, Routes, BrowserRouter as Router } from 'react-router-dom';
import BoardRouteController from "./board/boardRoute/BoardRouteController"
import UserRouteController from "./user/userRoute/UserRouteController";
import Authentication from "./user/authentication";

const Main = () => {
    return (
        <>
            <BoardRouteController />
            <UserRouteController />
        </>
    )
}
export default Main