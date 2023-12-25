import React, { useState } from "react";
import styled from "styled-components";

interface BiddingFormProps {
  onBidSubmit: (bidAmount: number) => void;
}

const BiddingFormContainer = styled.div`
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const BidInput = styled.input`
  width: 100%;
  font-size: 16px;
  margin-bottom: 8px;
`;

const BidButton = styled.button`
  font-size: 16px;
  background-color: #007bff;
  color: #fff;
  padding: 8px 16px;
  cursor: pointer;
`;

const BiddingForm: React.FC<BiddingFormProps> = ({ onBidSubmit }) => {
  const [bidAmount, setBidAmount] = useState<number>(0);

  const handleBidSubmit = () => {
    // 필요한 경우 유효성 검사 수행
    onBidSubmit(bidAmount);
  };

  return (
    <BiddingFormContainer>
      <BidInput
        type="number"
        value={bidAmount}
        onChange={(e) => setBidAmount(Number(e.target.value))}
        placeholder="입찰 금액을 입력하세요"
      />
      <BidButton onClick={handleBidSubmit}>입찰하기</BidButton>
    </BiddingFormContainer>
  );
};

export default BiddingForm;
