// biddingService.ts
const API_BASE_URL = 'http://localhost:8086/bidding-service';

export interface BiddingItem {
  biddingPrice: number;
  biddingTime: string; // or Date, depending on the backend response format
  memberUuid: string;
  name: string;
  stock: number;
  source: string;
  image: string;
}

export const getBiddingList = async (): Promise<BiddingItem[]> => {
  try {
    const memberUuid = "8275b931-e608-4075-8690-51d3ffb267b1";
    const response = await fetch(`${API_BASE_URL}/${memberUuid}/bidding`);

    if (!response.ok) {
      throw new Error(`Failed to fetch bidding list. Status: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error('입찰 목록을 가져오는 중 오류 발생:', error);
    throw error;
  }
};
