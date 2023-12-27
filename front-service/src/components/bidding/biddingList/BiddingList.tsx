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

interface BiddingListProps {
  memberUuid: string;
}

const BiddingList: React.FC<BiddingListProps> = ({ memberUuid }) => {
  const [biddingList, setBiddingList] = useState<BiddingResponseDto[]>([]);

  useEffect(() => {
    const fetchBiddingList = async () => {
      try {
        const response = await fetch(`/api/bidding-service/${memberUuid}/bidding`);
        if (!response.ok) {
          throw new Error('Failed to fetch bidding list');
        }
        const data = await response.json();
        setBiddingList(data);
      } catch (error) {
        console.error('Error fetching bidding list:', error);
      }
    };

    fetchBiddingList();
  }, [memberUuid]);

  return (
    <div>
      <h2>Bidding List for Member {memberUuid}</h2>
      <ul>
        {biddingList.map((biddingItem, index) => (
          <li key={index}>
            <p>Bidding Price: {biddingItem.biddingPrice}</p>
            <p>Bidding Time: {biddingItem.biddingTime}</p>
            <p>Member UUID: {biddingItem.memberUuid}</p>
            <p>Name: {biddingItem.name}</p>
            <p>Stock: {biddingItem.stock}</p>
            <p>Source: {biddingItem.source}</p>
            <img src={biddingItem.image} alt={`Image for ${biddingItem.name}`} />
          </li>
        ))}
      </ul>
    </div>
  );
};

export default BiddingList;
