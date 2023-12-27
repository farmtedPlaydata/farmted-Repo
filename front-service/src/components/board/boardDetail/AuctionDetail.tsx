import React, { useState } from "react";
import styled from "styled-components";
import BiddingModal from "../../bidding/ModalComponent";

interface AuctionDetailProps {
  auctionDetail: AuctionDetail;
  boardUuid: string;
}

interface AuctionDetail {
  auctionPrice: number;
  auctionBuyer: string;
  auctionDeadline: Date;
  auctionStatus?: boolean;
}

const AuctionDetailContainer = styled.div`
  margin-top: 16px;
`;

const AuctionDetailTable = styled.table`
  width: 100%;
  margin-bottom: 16px; /* 테이블과 입찰 버튼 간격 조절을 위한 마진 추가 */
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

const PlaceBidButton = styled.button`
  background-color: #007bff;
  color: #fff;
  border: none;
  padding: 10px 15px;
  cursor: pointer;
  font-size: 16px;
  margin-right: 10px;

  &:hover {
    background-color: #0056b3;
  }
`;

const AuctionDetailComponent: React.FC<AuctionDetailProps> = ({ auctionDetail , boardUuid}) => {
  const [isBiddingModalOpen, setIsBiddingModalOpen] = useState(false);

  const openBiddingModal = () => {
    setIsBiddingModalOpen(true);
  };

  const closeBiddingModal = () => {
    setIsBiddingModalOpen(false);
  };

  return (
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

      {/* "파란색 입찰 신청" 버튼 */}
      <PlaceBidButton onClick={openBiddingModal}>파란색 입찰 신청</PlaceBidButton>
      {/* "파란색 입찰 신청" 버튼 끝 */}
      
      {isBiddingModalOpen && (
        <BiddingModal
          onClose={closeBiddingModal}
          isOpen={isBiddingModalOpen}
          closeModal={closeBiddingModal}
          boardUuid={boardUuid} // 적절한 값을 전달
          memberUuid={"53bc6f2e-7686-4791-9591-f66762210c46"} // 적절한 값을 전달
          modalContent={`최상위 입찰가: ${1000}, 마감 시간: ${"2024-01-25 21:45:00.000000"}`}
        />
      )}
    </AuctionDetailContainer>
  );
};

export default AuctionDetailComponent;
