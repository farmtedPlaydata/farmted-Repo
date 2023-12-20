package com.farmted.auctionservice.service;

import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.repository.AuctionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class BiddingServiceTest {

        @Mock
        private BiddingService biddingService;

        @Mock AuctionRepository auctionRepository;

        @Test
        public void testConcurrentBidding() throws InterruptedException {
            // 가격이 1000원 차이나는 테스트를 위해 가격 설정
            BigDecimal basePrice = BigDecimal.valueOf(10000);

            // 스레드 수
            int threadCount = 5;

            // CountDownLatch를 사용하여 모든 스레드가 동시에 시작하도록 함
            CountDownLatch startLatch = new CountDownLatch(1);
            // CountDownLatch를 사용하여 모든 스레드가 작업을 완료할 때까지 대기
            CountDownLatch finishLatch = new CountDownLatch(threadCount);

            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        // 모든 스레드가 준비될 때까지 대기
                        startLatch.await();

                        // 각 스레드는 1000원씩 차이 나는 가격으로 입찰을 생성
                        BiddingCreateRequestDto requestDto = BiddingCreateRequestDto.builder()
                                .biddingPrice(basePrice.add(BigDecimal.valueOf(1000)))
                                .build();

                        // 입찰 생성
                        biddingService.createBidding(requestDto, "user123","123");

                        // 작업 완료
                        finishLatch.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            // 모든 스레드가 동시에 시작하도록 CountDownLatch를 0으로 설정
            startLatch.countDown();

            // 모든 스레드가 작업을 완료할 때까지 대기
            finishLatch.await();

            // 모든 스레드의 입찰이 성공적으로 생성되었는지 확인
            assertThat(auctionRepository.findAuctionByBoardUuid("board123")).isEqualTo(threadCount);

            // 각 스레드에서 생성한 입찰 중 최고가가 올바르게 설정되었는지 확인
            BigDecimal expectedMaxPrice = basePrice.add(BigDecimal.valueOf((threadCount - 1) * 1000));
            assertThat(auctionRepository.findMaxAuctionPrice("board123")).isEqualTo(expectedMaxPrice);

            executorService.shutdown();
        }
    }

