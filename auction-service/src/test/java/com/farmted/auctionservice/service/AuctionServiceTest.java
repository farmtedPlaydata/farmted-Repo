package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Auction;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionStatusResponseDto;
import com.farmted.auctionservice.repository.AuctionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        LocalDate currentDate = LocalDate.now();
        Auction auction1 = Auction.builder()
                .auctionUuid(UUID.randomUUID().toString())
                .auctionPrice(BigDecimal.valueOf(10000))
                .auctionBuyer(null)
                .auctionDeadline(currentDate)
                .auctionStatus(false)
                .memberUuid("member123")
                .boardUuid("board456")
                .productUuid("product789")
                .build();

        // 저장
        auctionRepository.save(auction1);
    }

    @Test
    @DisplayName("경매 마감 날짜에 맞춰서 상태 값 변경 스케쥴러 확인")
    void changeAuction_shouldChangeAuctionStatus() {
        // 테스트 대상 메서드 호출
        List<AuctionStatusResponseDto> result = auctionService.changeAuction();

        // 결과 검증
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuctionStatus()).isEqualTo(true);
    }
}