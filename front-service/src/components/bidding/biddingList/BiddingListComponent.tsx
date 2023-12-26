// BiddingListComponent.tsx
import React, { useEffect, useState } from "react";
import { getBiddingList, BiddingItem } from "./biddingService";
import {
  BiddingListContainer,
  BiddingListItem,
  BiddingItemName,
  BiddingItemPrice,
  BiddingItemTime,
} from "./BiddingListStyled";

const BiddingListComponent: React.FC = () => {
  const [biddingList, setBiddingList] = useState<BiddingItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBiddingList = async () => {
      try {
        const result = await getBiddingList();
        setBiddingList(result);
      } catch (error) {
        console.error('오류 발생:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchBiddingList();
  }, []);

  return (
    <BiddingListContainer>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <ul>
          {biddingList.map((item) => (
            <BiddingListItem key={item.memberUuid}>
              <BiddingItemName>{item.name}</BiddingItemName>
              <BiddingItemPrice>Bidding Price: {item.biddingPrice}</BiddingItemPrice>
              <BiddingItemTime>Bidding Time: {item.biddingTime}</BiddingItemTime>
            </BiddingListItem>
          ))}
        </ul>
      )}
    </BiddingListContainer>
  );
};

export default BiddingListComponent;
