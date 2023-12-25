import React, { useState } from 'react';
import styled from 'styled-components';
import CategoryList from './boardUtil/CategoryList';
import SearchBar from './boardUtil/SearchBar';
import WriteButton from './boardUtil/WriteButton';
import { BoardType } from './boardUtil/BoardType';
import BiddingModal from '../../bidding/ModalComponent';

interface Props {
  totalBoard?: number;
  category: BoardType;
  onCategoryChange: (newCategory: BoardType) => void;
}

const BoardHeadDiv = styled.div`
  padding-top: 48px;
  padding-left: 32px;
  padding-right: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #20c997;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const HeadInfoDiv = styled.div`
  h1 {
    margin: 0;
    font-size: 36px;
    color: #343a40;
  }
  .category {
    margin-top: 4px;
    color: #868e96;
    font-size: 21px;
  }
  .result-size {
    color: #20c997;
    font-size: 18px;
    margin-top: 40px;
    font-weight: bold;
  }
`;

const SearchContainer = styled.div`
  display: flex;
  flex-direction: column; /* 추가: 수직 정렬 */
  align-items: flex-end; /* 추가: 우측 정렬 */
  margin-top: 20px;
`;

const BlueButton = styled.button`
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

const BoardHead = (props: Props) => {
  const { category, totalBoard = 0, onCategoryChange } = props;
  const [isBiddingModalOpen, setIsBiddingModalOpen] = useState(false);

  const openBiddingModal = () => {
    setIsBiddingModalOpen(true);
  };

  const closeBiddingModal = () => {
    setIsBiddingModalOpen(false);
  };

  return (
    <BoardHeadDiv>
      <HeadInfoDiv>
        <h1>검색 결과</h1>
        <div className="category">{category}</div>
        <div className="result-size">{totalBoard}개 검색됨</div>
      </HeadInfoDiv>
      <SearchContainer>
        {/* "파란색 입찰 신청" 버튼 */}
        <BlueButton onClick={openBiddingModal}>파란색 입찰 신청</BlueButton>
        {/* "파란색 입찰 신청" 버튼 끝 */}
        <WriteButton />
        <SearchBar onSearch={(query) => console.log('Searching with query:', query)} />
        <CategoryList categories={category} onCategoryChange={onCategoryChange} />
      </SearchContainer>
      {/* 모달이 열려있을 때 BiddingModal을 렌더링 */}
      {isBiddingModalOpen && (
        <BiddingModal
        onClose={closeBiddingModal}
        isOpen={isBiddingModalOpen}
        closeModal={closeBiddingModal}
        boardUuid={"20231115"} // 적절한 값을 전달
        memberUuid={"53bc6f2e-7686-4791-9591-f66762210c46"} // 적절한 값을 전달
        modalContent={`입찰가: ${1000}, 마감 시간: ${"2024-01-25 21:45:00.000000"}`}
    
        />
      )}
    </BoardHeadDiv>
  );
};

export default BoardHead;
