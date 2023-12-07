package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.feignClient.AuctionFeignClient;
import com.farmted.boardservice.util.feignConverter.FeignConverter;
import com.farmted.boardservice.vo.AuctionVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@DisplayName("Auction-Service 테스트 코드")
class AuctionServiceTest {
    private final AuctionFeignClient auctionFeignClient = mock(AuctionFeignClient.class);
    private final FeignConverter<AuctionVo> auctionConverter;

    @Autowired
    AuctionServiceTest(FeignConverter<AuctionVo> auctionConverter) {
        this.auctionConverter = auctionConverter;
    }

    private AuctionService auctionService;

    @BeforeEach
    void setUp(){auctionService = new AuctionService(auctionFeignClient, auctionConverter);}

    @Test
    @DisplayName("경매에 대한 페이징 리스트")
    void getAuctionList() {
    }

    @Test
    @DisplayName("판매자의 낙찰 내역 조회")
    void getSellerAuctionList() {
    }

    @Test
    @DisplayName("구매자의 낙찰 내역 조회")
    void getBuyerAuctionList() {
    }
}