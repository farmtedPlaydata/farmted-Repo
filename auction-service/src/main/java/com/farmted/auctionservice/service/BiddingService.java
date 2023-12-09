package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.dto.ResponseBiddingDto.BiddingResponseDto;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.repository.BiddingRepository;
import com.farmted.auctionservice.vo.RedisBidding;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BiddingService {
    private final BiddingRepository biddingRepository;
    private final RedisTemplate<String, RedisBidding> redisTemplate;

    public void createBidding(BiddingCreateRequestDto biddingCreateRequestDto, String boardUuid, String memberUuid){

        // 레디스 경우 후
        RedisBidding redisBidding = biddingCreateRequestDto.toRedisEntity(boardUuid, memberUuid);
        redisTemplate.opsForValue().set("bbb", redisBidding);


        // JPA 이동
        //TODO: 경매 종료 상품에 대해서는 입찰 불가능? 예외처리
        Bidding bidding = biddingCreateRequestDto.toEntity(boardUuid,memberUuid);

// @PrePersist 어노테이션이 사용된 메소드는 엔터티가 저장되기 전에 호출이니깐 여기서 수행?
        biddingRepository.save(bidding);

    }

   public List<BiddingResponseDto> getBiddingList(String memberUuid){
       List<Bidding> biddingByMemberUuid = biddingRepository.findBiddingByMemberUuid(memberUuid);
       List<RedisBidding> redisBiddingList = new ArrayList<>();
       Set<String> biddingUuids = redisTemplate.keys("*");  // 모든 키
       System.out.println(biddingUuids);


       return biddingByMemberUuid.stream()
               .map(BiddingResponseDto::new)
               .toList();
   }




}
