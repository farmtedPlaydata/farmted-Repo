package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Auction;
import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.dto.ResponseBiddingDto.BiddingResponseDto;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.repository.AuctionRepository;
import com.farmted.auctionservice.repository.BiddingRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class BiddingService {
    private final BiddingRepository biddingRepository;
    private final AuctionRepository auctionRepository;
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String PREFIX = "Auction-BIDDING::";

    public void createBidding(BiddingCreateRequestDto biddingCreateRequestDto, String boardUuid, String memberUuid) {
        Bidding savedBidding = biddingCreateRequestDto.toEntity(boardUuid, memberUuid);

        // Redis 비동기 큐에 이벤트 추가
        String queueName = "auctionQueue:" + savedBidding.getBoardUuid();
        redisTemplate.opsForList().rightPush(queueName, savedBidding);

        // 여러 클라이언트가 메시지를 처리하고 최상위 낙찰자 추출
        processAuctionEvents(savedBidding.getBoardUuid());
    }

    private void processAuctionEvents(String boardUuid) {
        String lockName = PREFIX + boardUuid;

        // Redis 분산 락 획득
        RLock lock = redissonClient.getLock(lockName);
        lock.lock();

        try {
            String queueName = "auctionQueue:" + boardUuid;
            // Redis 비동기 큐에서 이벤트를 소비하고 처리하는 로직
            while (redisTemplate.opsForList().size(queueName) > 0) {
                Bidding topBid = (Bidding) redisTemplate.opsForList().leftPop(queueName);
                updateTopBidder(topBid);
            }
        } finally {
            // Redis 분산 락 해제
            lock.unlock();
        }
    }

    private void updateTopBidder(Bidding savedBidding) {
        String boardUuid = savedBidding.getBoardUuid();
        String lockName = "auctionLock:" + boardUuid;
        RScoredSortedSet<Long> topBidders = redissonClient.getScoredSortedSet("topBidders");

        // Redis 분산 락 획득
        RLock lock = redissonClient.getLock(lockName);
        lock.lock();

        try {
            topBidders.add(savedBidding.getBiddingPrice(), savedBidding.getBiddingId());
            while (topBidders.size() > 1) {
                topBidders.pollFirst();
            }

            // 최상위 낙찰자 1명만을 Auction DB에 저장
            if (savedBidding.getBiddingId() == topBidders.first()) {
                Auction auctionByBoardUuid = auctionRepository.findAuctionByBoardUuid(savedBidding.getBoardUuid());
                auctionByBoardUuid.setBiddingTop(savedBidding.getBiddingPrice(),savedBidding.getMemberUuid());
            }

            // 데이터베이스 업데이트는 비동기로 처리
            CompletableFuture.runAsync(() -> {
                biddingRepository.save(savedBidding);
            });
        } finally {
            // Redis 분산 락 해제
            lock.unlock();
        }
    }


    public List<BiddingResponseDto> getBiddingList(String memberUuid){
       List<Bidding> biddingByMemberUuid = biddingRepository.findBiddingByMemberUuid(memberUuid);
       return biddingByMemberUuid.stream()
               .map(BiddingResponseDto::new)
               .toList();
   }




}
