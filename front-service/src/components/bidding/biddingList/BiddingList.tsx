import React, { useEffect, useState } from 'react';
import styled from 'styled-components';

interface BiddingResponseDto {
  biddingPrice: number;
  biddingTime: string;
  memberUuid: string;
  name: string;
  stock: number;
  source: string;
  image: string;
}

const AuctionDetailContainer = styled.div`
  margin-top: 16px;
`;

const AuctionDetailTable = styled.table`
  width: 100%;
  margin-bottom: 16px;
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

const BiddingList = () => {
  const [biddingList, setBiddingList] = useState<BiddingResponseDto[]>([]);
  const memberUuid = '6036e646-97d3-4e53-aee4-f88e00b57a4b';

  useEffect(() => {
    let isMounted = true;

    const fetchBiddingList = async () => {
      try {
        const response = await fetch(`/api/auction-service/bidding-service/${memberUuid}/bidding`);

        if (!response.ok) {
          const errorText = await response.text();
          console.error('입찰 목록을 가져오는 데 실패했습니다:', errorText);
          throw new Error('입찰 목록을 가져오는 데 실패했습니다');
        }

        const data = await response.json();

        if (isMounted) {
          console.log('Data:', data);
          setBiddingList(data);
        }
      } catch (error) {
        console.error('입찰 목록을 가져오는 중 오류 발생:', error);
      }
    };

    fetchBiddingList();

    return () => {
      isMounted = false;
    };
  }, [memberUuid]);

  return (
    <AuctionDetailContainer>
      <h2>입찰 목록입니다</h2>
      <h3>회원 {memberUuid}의 입찰 목록</h3>
      {biddingList.length > 0 ? (
        <AuctionDetailTable>
          <thead>
            <AuctionDetailRow>
              <AuctionDetailTitle>입찰 가격</AuctionDetailTitle>
              <AuctionDetailTitle>입찰 시간</AuctionDetailTitle>
              <AuctionDetailTitle>이름</AuctionDetailTitle>
              <AuctionDetailTitle>재고</AuctionDetailTitle>
              <AuctionDetailTitle>출처</AuctionDetailTitle>
              <AuctionDetailTitle>이미지</AuctionDetailTitle>
            </AuctionDetailRow>
          </thead>
          <tbody>
            {biddingList.map((biddingItem, index) => (
              <AuctionDetailRow key={index}>
                <AuctionDetailContent>{biddingItem.biddingPrice}</AuctionDetailContent>
                <AuctionDetailContent>{biddingItem.biddingTime}</AuctionDetailContent>
                <AuctionDetailContent>{biddingItem.name}</AuctionDetailContent>
                <AuctionDetailContent>{biddingItem.stock}</AuctionDetailContent>
                <AuctionDetailContent>{biddingItem.source}</AuctionDetailContent>
                <AuctionDetailContent>
                  <img src={biddingItem.image} alt={`${biddingItem.name}에 대한 이미지`} />
                </AuctionDetailContent>
              </AuctionDetailRow>
            ))}
          </tbody>
        </AuctionDetailTable>
      ) : (
        <p>입찰 목록이 없습니다.</p>
      )}
    </AuctionDetailContainer>
  );
};

export default BiddingList;
