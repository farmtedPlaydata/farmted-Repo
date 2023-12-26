// BiddingListStyled.ts
import styled from 'styled-components';


export const BiddingListContainer = styled.div`
  max-width: 600px;
  margin: 0 auto;
`;

export const BiddingListItem = styled.li`
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid #ddd;
`;

export const BiddingItemName = styled.div`
  font-size: 18px;
  font-weight: bold;
`;

export const BiddingItemPrice = styled.div`
  margin-top: 10px;
`;

export const BiddingItemTime = styled.div`
  color: #666;
  margin-top: 5px;
`;
