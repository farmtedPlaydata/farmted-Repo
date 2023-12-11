package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.dto.ResponseBiddingDto.BiddingResponseDto;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.repository.BiddingRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BiddingService {
    private final BiddingRepository biddingRepository;

    private final String PREFIX = "Auction-BIDDING::";

    public void createBidding(BiddingCreateRequestDto biddingCreateRequestDto, String boardUuid, String memberUuid){
        String lockName = PREFIX + boardUuid;
       // RLock lock = redissonClient.getLock(lockName);


        // JPA 이동
        //TODO: 경매 종료 상품에 대해서는 입찰 불가능? 예외처리
        Bidding bidding = biddingCreateRequestDto.toEntity(boardUuid,memberUuid);


        biddingRepository.save(bidding);

    }

   public List<BiddingResponseDto> getBiddingList(String memberUuid){
       List<Bidding> biddingByMemberUuid = biddingRepository.findBiddingByMemberUuid(memberUuid);
       return biddingByMemberUuid.stream()
               .map(BiddingResponseDto::new)
               .toList();
   }




}
