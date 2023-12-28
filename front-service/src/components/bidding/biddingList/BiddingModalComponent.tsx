// BiddingModal.tsx

import React, { useState } from 'react';
import styled from 'styled-components';

interface BiddingModalProps {
  boardUuid: string;
  memberUuid: string;
  onClose: () => void;
  isOpen: boolean;
  closeModal: () => void;
  modalContent: string;
}

const ModalWrapper = styled.div`
  border: 2px solid #007bff;
  padding: 20px;
  background-color: #fff;
  width: 600px;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

const BiddingModal: React.FC<BiddingModalProps> = ({ boardUuid, memberUuid, onClose, isOpen, closeModal, modalContent }) => {
  const [newBiddingPrice, setNewBiddingPrice] = useState<number>();
  const [memberPrice, setMemberPrice] = useState<number>(10000);
  const [biddingAutoPrice, setBiddingAutoPrice] = useState<number | null>(null);
  const [warningMessage, setWarningMessage] = useState<string | null>(null);

  const handleBiddingSubmit = async () => {
    if (newBiddingPrice && newBiddingPrice > memberPrice) {
      setWarningMessage('입찰 가격이 회원 가격보다 큽니다. 다시 입력해주세요.');
      return;
    }

    try {
      const response = await fetch(`/api/auction-service/bidding-service/bid/${boardUuid}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'UUID': memberUuid,
        },
        body: JSON.stringify({
          biddingPrice: newBiddingPrice,
          memberPrice,
          biddingAutoPrice,
        }),
      });

      if (response.ok) {
        // 성공적으로 입찰이 이루어졌을 때 모달을 닫습니다.
        onClose();
      } else {
        // 에러 처리 로직을 추가하세요.
        console.error('입찰 실패:', response);
      }
    } catch (error) {
      // 네트워크 오류 또는 기타 예외 처리 로직을 추가하세요.
      console.error('네트워크 오류');
    }
  };

  return (
    <ModalWrapper>
      {/* 모달 내용 및 폼 */}
      <label htmlFor="biddingPrice">추가 입찰 희망 가격:</label>
      <input type="number" id="biddingPrice" value={newBiddingPrice} onChange={(e) => setNewBiddingPrice(+e.target.value)} />

      {/* 다른 입력 필드 및 버튼들은 이곳에 추가하세요. */}

      {warningMessage && <div style={{ color: 'red' }}>{warningMessage}</div>}

      <button onClick={handleBiddingSubmit}>입찰 신청</button>
      <button onClick={onClose}>취소</button>

      {/* modalContent 표시 */}
      <div>이전 낙찰 내역:{modalContent}</div>
      <div>현재 잔고: {memberPrice}</div>
    </ModalWrapper>
  );
};

export default BiddingModal;
