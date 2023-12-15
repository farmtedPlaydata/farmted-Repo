package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BiddingServiceTest {

    @Autowired
    private BiddingService biddingService;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testConcurrentCreateBiddingAndTopBidderUpdate() throws InterruptedException {
        // 생성할 스레드 수
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 여러 스레드를 동시에 실행
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                // 각 스레드는 다른 입찰 금액으로 입찰을 생성
                BiddingCreateRequestDto createRequestDto = new BiddingCreateRequestDto();
                Integer bidAmount = (int) Math.random() * 100; // Random bid amount between 0 and 100
                createRequestDto.setBiddingPrice(bidAmount);

                String boardUuid = "sampleBoardUuid";
                String memberUuid = "sampleMemberUuid";

                // 입찰을 동시에 생성
                biddingService.createBidding(createRequestDto, boardUuid, memberUuid);
            });
        }


        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        RMap<String, Bidding> bidMap = redissonClient.getMap("bids");
        RScoredSortedSet<Long> topBidders = redissonClient.getScoredSortedSet("topBidders");

        // 예상 크기 확인
        assertEquals(threadCount, bidMap.size());
        // 가정: 하나의 입찰만 존재
        assertEquals(1, topBidders.size());

        // 최종 상태 출력
        System.out.println("최종 상태 - bidMap: " + bidMap);
        System.out.println("최종 상태 - topBidders: " + topBidders);
    }

}