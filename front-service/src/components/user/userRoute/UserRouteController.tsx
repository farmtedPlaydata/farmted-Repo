import { Route, Routes } from "react-router-dom"
import React from "react"
import Authentication from "../authentication";

const UserRouteController = () => {
    return(
        <Routes>

            <Route path={'/auth'} element={<Authentication />} />
        </Routes>
    )
}
export default UserRouteController;