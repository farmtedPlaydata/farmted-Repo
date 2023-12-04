package com.farmted.auctionservice.service;


import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.dto.ResponseBiddingDto.BiddingResponseDto;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.repository.BiddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BiddingService {
    private final BiddingRepository biddingRepository;

    public void createBidding(BiddingCreateRequestDto biddingCreateRequestDto, String boardUuid, String memberUuid){
        //TODO: 응찰 금액이 잔고를 넘는지 확인?

        Bidding bidding = biddingCreateRequestDto.toEntity(boardUuid,memberUuid);
        //TODO: 경매 종료 상품에 대해서는 입찰 불가능?

        biddingRepository.save(bidding);
    }

   public List<BiddingResponseDto> getBiddingList(String memberUuid){
       List<Bidding> biddingByMemberUuid = biddingRepository.findBiddingByMemberUuid(memberUuid);
       return biddingByMemberUuid.stream()
               .map(BiddingResponseDto::new)
               .toList();
   }
}
