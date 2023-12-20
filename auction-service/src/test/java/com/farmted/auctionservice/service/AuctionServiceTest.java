package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Auction;
import com.farmted.auctionservice.repository.AuctionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuctionServiceTest {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private AuctionRepository auctionRepository;

    @BeforeEach
    void setUp() {
        // 가짜 데이터 생성
        LocalDateTime currentDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Auction auction1 = Auction.builder()
                .auctionUuid(UUID.randomUUID().toString())
                .auctionPrice(BigDecimal.valueOf(10000))
                .auctionBuyer(null)
                .auctionDeadline(currentDate)
                .auctionStatus(false)
                .memberUuid("member123")
                .boardUuid("board456")
                .productUuid("fc734824-da40-4844-84d1-977513357c61")
                .build();

        Auction auction2 = Auction.builder()
                .auctionUuid(UUID.randomUUID().toString())
                .auctionPrice(BigDecimal.valueOf(10000))
                .auctionBuyer(null)
                .auctionDeadline(currentDate)
                .auctionStatus(false)
                .memberUuid("member345")
                .boardUuid("board456")
                .productUuid("fc734824-da40-4844-84d1-977513357c61")
                .build();


        // 저장
        auctionRepository.save(auction1);
        auctionRepository.save(auction2);
    }

    @Test
    @DisplayName("경매 마감 날짜에 맞춰서 상태 값 변경 스케쥴러 확인")
    void changeAuction_shouldChangeAuctionStatus() {
        // 테스트 대상 메서드 호출
        List<Auction> result = auctionService.changeAuction();

        // 결과 검증
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAuctionStatus()).isEqualTo(true);
    }
}