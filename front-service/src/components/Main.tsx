import React from "react"
import { Link, Route, Routes, BrowserRouter as Router } from 'react-router-dom';
import BoardRouteController from "./board/boardRoute/BoardRouteController"
import BiddingRouteController from "./bidding/biddingRoute/BiddingRouteController";

const Main = () => {
    return (
        <>
            <BoardRouteController/>
            {/* <Router>
                <Routes>
                    <Route path="/board" element={<BoardRouteController />} />
                    <Route path="/bidding" element={<BiddingRouteController />} />
                </Routes>
            </Router> */}
        </>
    )
}
export default Main