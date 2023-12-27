import { Route, Routes } from "react-router-dom"
import React from "react"
import BoardMain from "./BoardMain"
import BoardBody from "../BoardBody"
import CreateBoard from "../createBoard/CreateBoard"
import BoardDetail from "../boardDetail/BoardDetail"
import Sidebar from "../../sidebar/sidebar";
import Authentication from "../../user/authentication";
import BiddingList from "../../bidding/biddingList/BiddingList"

const BoardRouteController = () => {
    return
        <>
            {/*<Routes>*/}
            {/*    <Route path="/" element={<Sidebar path={"/"} menuName={"Main"}/>}/>*/}
            {/*</Routes>*/}
            <Routes>
                <Route path="/" element={<BoardBody/>}>
                    <Route path='/' element={<Authentication />} />
                    <Route path="/boards" element={<BoardMain/>}/>
                    <Route path="/boards/write" element={<CreateBoard/>}/>
                    <Route path="/boards/:boardUUID" element={<BoardDetail />}/>
                </Route>
            </Routes>
        </>

    )
}
export default BoardRouteController