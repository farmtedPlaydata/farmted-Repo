import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

interface BiddingResponseDto {
  biddingPrice: number;
  biddingTime: string;
  memberUuid: string;
  name: string;
  stock: number;
  source: string;
  image: string;
}

const BiddingList = () => {
  const [biddingList, setBiddingList] = useState<BiddingResponseDto[]>([]);
  // const { memberUuid } = useParams<{ memberUuid: string }>();
  const memberUuid = '6036e646-97d3-4e53-aee4-f88e00b57a4b';

  useEffect(() => {
    let isMounted = true; // Flag to check if the component is mounted

  

    const fetchBiddingList = async () => {
      
      try {
        const response = await fetch(`/bidding/bidding-service/${memberUuid}/bidding`);
    
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

    // Cleanup function to set isMounted to false when component is unmounted
    return () => {
      isMounted = false;
    };
  }, [memberUuid]);

  return (
    <div>
      <h2>입찰 목록입니다</h2>
      <h3>회원 {memberUuid}의 입찰 목록</h3>
      {biddingList.length > 0 ? (
        <ul>
          {biddingList.map((biddingItem, index) => (
            <li key={index}>
              <p>입찰 가격: {biddingItem.biddingPrice}</p>
              <p>입찰 시간: {biddingItem.biddingTime}</p>
              <p>회원 UUID: {biddingItem.memberUuid}</p>
              <p>이름: {biddingItem.name}</p>
              <p>재고: {biddingItem.stock}</p>
              <p>출처: {biddingItem.source}</p>
              <img src={biddingItem.image} alt={`${biddingItem.name}에 대한 이미지`} />
            </li>
          ))}
        </ul>
      ) : (
        <p>입찰 목록이 없습니다.</p>
      )}
    </div>
  );
};

export default BiddingList;
