package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Auction;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.repository.AuctionRepository;
import com.farmted.auctionservice.repository.BiddingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class BiddingServiceTest {

        @Autowired
        private BiddingService biddingService;

        @Autowired AuctionRepository auctionRepository;
        @Autowired BiddingRepository biddingRepository;

        @BeforeEach
        public void setup() {
            // Creating test data for Auction entity
            Auction auction1 = Auction.builder()
                    .auctionPrice(BigDecimal.valueOf(500))
                    .auctionBuyer(null)
                    .auctionDeadline(LocalDateTime.now().plusDays(1)) // Set a future date for testing
                    .memberUuid("seller123")
                    .boardUuid("board123")
                    .productUuid("product789")
                    .build();
            // Saving test data to the repository
            auctionRepository.save(auction1);
        }



        @Test
        public void 동시에100명이경매를주문하는상황에서최고가선별() throws InterruptedException {

            // 동시 요청의 개수
            final int threadCount = 100;

            // 32쓰레드가 동시에 요청넣도록 풀 설정
            ExecutorService executorService = Executors.newFixedThreadPool(32);
            // 요청 마친 쓰레드는 전체 쓰레드 풀이 끝날때까지 대기하도록 처리
            CountDownLatch countDownLatch = new CountDownLatch(threadCount);

            for (int i = 0; i < 100; i++) {
                int index = i; // 가변적인 상태로 변수 선언
                executorService.submit(() -> {
                    try {
                        BiddingCreateRequestDto dto = new BiddingCreateRequestDto(BigDecimal.valueOf(1000+index),100,0);
                        biddingService.createBidding(dto, "board123", "member");
                    } catch (Exception e) {
                        // 예외 처리 로직 추가
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }

            countDownLatch.await(); // 모든 쓰레드의 호출이 끝나면 쓰레드 풀 자체를 종료
            //then
            // 모든 스레드의 입찰이 성공적으로 생성되었는지 확인
            assertThat(biddingRepository.findBiddingByBoardUuid("board123").size()).isEqualTo(threadCount);

            // 각 스레드에서 생성한 입찰 중 최고가가 올바르게 설정되었는지 확인
            assertThat(auctionRepository.findMaxAuctionPrice("board123")).isEqualByComparingTo(new BigDecimal("1099.00"));

        }
    }

