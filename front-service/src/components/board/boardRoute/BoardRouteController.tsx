import { Route, Routes } from "react-router-dom"
import React from "react"
import BoardMain from "./BoardMain"
import BoardBody from "../BoardBody"
import CreateBoard from "../createBoard/CreateBoard"
import BoardDetail from "../boardDetail/BoardDetail"
import Authentication from "../../user/authentication";
import BiddingList from "../../bidding/biddingList/BiddingList"

const BoardRouteController = () => {
    return(
        <Routes>
            <Route path="/" element={<BoardBody/>}>
                {/* <Route path="/" element={<Authentication/>}/> */}
                <Route path="/" element={<BoardMain/>}/>
                <Route path="/boards/write" element={<CreateBoard/>}/>
                <Route path="/boards/:boardUUID" element={<BoardDetail />}/>
                <Route path="/bidding/:memberUuid" element={<BiddingList memberUuid={"yourDefaultValueHere"} />} // 여기에 기본값 또는 비워둘 수 있습니다.
        />
            </Route>
        </Routes>
    )
}
export default BoardRouteController