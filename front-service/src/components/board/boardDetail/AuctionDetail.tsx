import React from "react";
import styled from "styled-components";

interface AuctionDetailProps {
  auctionDetail: AuctionDetail;
}

interface AuctionDetail {
  auctionPrice: number;
  auctionBuyer: string;
  auctionDeadline: Date;
  auctionStatus?: boolean;
}

const AuctionDetailContainer = styled.td`
  margin-top: 16px;
  display: flex;
  border-bottom: 1px solid #000;
`;

const AuctionDetailTable = styled.table`
  width: 100%;
`;

const AuctionDetailRow = styled.tr`
  display: flex;
  justify-content: space-between;
`;

const AuctionDetailTitle = styled.th`
  font-size: 18px;
  margin-bottom: 8px;
  text-align: left;
`;

const AuctionDetailContent = styled.td`
  font-size: 16px;
  margin-bottom: 8px;
  text-align: right;
`;

const AuctionDetailComponent: React.FC<AuctionDetailProps> = ({ auctionDetail }) => (
  <AuctionDetailContainer>
    <AuctionDetailTable>
      <tbody>
        <AuctionDetailRow>
          <AuctionDetailTitle>입찰가</AuctionDetailTitle>
          <AuctionDetailContent>{auctionDetail.auctionPrice}</AuctionDetailContent>
        </AuctionDetailRow>
        <AuctionDetailRow>
          <AuctionDetailTitle>낙찰자</AuctionDetailTitle>
          <AuctionDetailContent>{auctionDetail.auctionBuyer}</AuctionDetailContent>
        </AuctionDetailRow>
        <AuctionDetailRow>
          <AuctionDetailTitle>마감 시간</AuctionDetailTitle>
          <AuctionDetailContent>{auctionDetail.auctionDeadline.toLocaleString()}</AuctionDetailContent>
        </AuctionDetailRow>
        {auctionDetail.auctionStatus !== undefined && (
          <AuctionDetailRow>
            <AuctionDetailTitle>경매 상태</AuctionDetailTitle>
            <AuctionDetailContent>{auctionDetail.auctionStatus ? '진행 중' : '종료됨'}</AuctionDetailContent>
          </AuctionDetailRow>
        )}
      </tbody>
    </AuctionDetailTable>
  </AuctionDetailContainer>
);

export default AuctionDetailComponent;
