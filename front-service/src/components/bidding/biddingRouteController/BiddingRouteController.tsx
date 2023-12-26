// BiddingRouteController.tsx
import React from "react";
import { Route, Routes } from 'react-router-dom';
import BiddingListComponent from "../biddingList/BiddingListComponent";


const BiddingRouteController: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<BiddingListComponent />} />
    </Routes>
  );
};

export default BiddingRouteController;
