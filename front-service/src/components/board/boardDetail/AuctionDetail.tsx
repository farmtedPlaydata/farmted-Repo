import React, { useState } from "react";
import styled from "styled-components";
import ModalComponent from "../../bidding/ModalComponent";
 // 모달 컴포넌트의 상대 경로를 적절히 수정

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
  cursor: pointer; // 클릭 가능한 내용을 나타내는 커서 스타일 추가
  color: #007bff; // 클릭 가능한 링크를 나타내는 색상 추가
`;



const AuctionDetailComponent: React.FC<AuctionDetailProps> = ({ auctionDetail }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <>
      <AuctionDetailContainer>
        <AuctionDetailTable>
          <tbody>
            <AuctionDetailRow>
              <AuctionDetailTitle>입찰가</AuctionDetailTitle>
              <AuctionDetailContent onClick={openModal}>{auctionDetail.auctionPrice}</AuctionDetailContent>
            </AuctionDetailRow>
            <AuctionDetailRow>
              <AuctionDetailTitle>낙찰자</AuctionDetailTitle>
              <AuctionDetailContent onClick={openModal}>{auctionDetail.auctionBuyer}</AuctionDetailContent>
            </AuctionDetailRow>
            <AuctionDetailRow>
              <AuctionDetailTitle>마감 시간</AuctionDetailTitle>
              <AuctionDetailContent onClick={openModal}>{auctionDetail.auctionDeadline.toLocaleString()}</AuctionDetailContent>
            </AuctionDetailRow>
            {auctionDetail.auctionStatus !== undefined && (
              <AuctionDetailRow>
                <AuctionDetailTitle>경매 상태</AuctionDetailTitle>
                <AuctionDetailContent onClick={openModal}>
                  {auctionDetail.auctionStatus ? '진행 중' : '종료됨'}
                </AuctionDetailContent>
              </AuctionDetailRow>
            )}
          </tbody>
        </AuctionDetailTable>
      </AuctionDetailContainer>

      <ModalComponent
      isOpen={isModalOpen}
      closeModal={closeModal}
      boardUuid="yourBoardUuid" // 적절한 값을 전달
      memberUuid="53bc6f2e-7686-4791-9591-f66762210c46" // 적절한 값을 전달
      onClose={closeModal}
      modalContent={`입찰가: ${auctionDetail.auctionPrice}, 마감 시간: ${auctionDetail.auctionDeadline.toLocaleString()}`}
    />
    </>
  );
}
export default AuctionDetailComponent;
// 여기서 경매 신청 alter 컴포넌트 연결?