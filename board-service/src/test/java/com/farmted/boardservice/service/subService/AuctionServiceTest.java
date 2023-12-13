package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.dto.response.detailDomain.ResponseGetAuctionDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetAuctionDto;
import com.farmted.boardservice.feignClient.AuctionFeignClient;
import com.farmted.boardservice.util.GlobalResponseDto;
import com.farmted.boardservice.util.feignConverter.FeignConverter;
import com.farmted.boardservice.vo.AuctionVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Import({AuctionFeignClient.class, AuctionService.class})
@DisplayName("Auction-Service 테스트 코드")
class AuctionServiceTest {
    @Mock
    private AuctionFeignClient auctionFeignClient;
    @Spy
    private FeignConverter<AuctionVo> auctionConverter;
    @InjectMocks
    private AuctionService auctionService;

    @Test
    @DisplayName("경매에 대한 페이징 리스트")
    void getAuctionList() {
        // given
        List<AuctionVo> auctionList = IntStream.rangeClosed(1, 3).mapToObj(
                i -> AuctionVo.builder()
                        .auctionPrice(1000 * i)
                        .auctionBuyer(i+" Dummy Buyer")
                        .auctionDeadline(LocalDateTime.now().plusDays(30))
                        .auctionStatus(true)
                        .build()).toList();
        int pageNo = 1;
        when(auctionFeignClient.findAuctionList(pageNo-1))
                .thenReturn(ResponseEntity.ok(GlobalResponseDto.listOf(auctionList)));
        // when
        List<ResponseGetAuctionDto> auctionDTOList = auctionService.getAuctionList(pageNo-1);
        // then
            // 요구한 값과 부합하는지
        IntStream.rangeClosed(1, 3)
                .forEach(i ->
                assertThat(auctionDTOList.get(i-1).getBuyerUuid()).isEqualTo(auctionList.get(i-1).auctionBuyer()));
            // 예측한만큼 실행되었는지
        verify(auctionFeignClient, times(1)).findAuctionList(eq(pageNo -1));
    }

    @Test
    @DisplayName("판매자의 낙찰 내역 조회")
    void getSellerAuctionList() {
        // given
        int pageNo = 1;
        String sellerUUID = "Buyer";
        List<AuctionVo> buyerAuctionList = IntStream.rangeClosed(1, 3).mapToObj(
                i -> AuctionVo.builder()
                        .auctionPrice(1000 * i)
                        .auctionBuyer(i + " Dummy Buyer")
                        .auctionDeadline(LocalDateTime.now().plusDays(30))
                        .auctionStatus(true)
                        .build()).toList();
        when(auctionFeignClient.findAuctionToSeller(sellerUUID,pageNo-1))
                .thenReturn(ResponseEntity.ok(GlobalResponseDto.listOf(buyerAuctionList)));
        // when
        List<ResponseGetAuctionDto> buyerAuctionDTOList = auctionService.getSellerAuctionList(sellerUUID, pageNo-1);
        // then
            // 요구한 값과 부합하는지
        IntStream.rangeClosed(1, 3)
                .forEach(i ->
                        assertThat(buyerAuctionDTOList.get(i-1).getBuyerUuid()).isEqualTo(i+ " Dummy Buyer"));
            // 예측한만큼 실행되었는지
        verify(auctionFeignClient, times(1)).findAuctionToSeller(sellerUUID, pageNo -1);
    }

    @Test
    @DisplayName("경매 상세 내역 조회")
    void getAuctionDetail() {
        // given
        String boardUUID = "boardUUID";
        AuctionVo auctionDetail = AuctionVo.builder()
                .auctionPrice(1000)
                .auctionBuyer("Buyer")
                .auctionDeadline(LocalDateTime.now().plusDays(30))
                .auctionStatus(true)
                .build();
        when(auctionFeignClient.findAuctionByBoardUuid(boardUUID))
                .thenReturn(ResponseEntity.ok(GlobalResponseDto.of(auctionDetail)));
        // when
        ResponseGetAuctionDetailDto auctionDTO = auctionService.getAuctionDetail(boardUUID);
        // then
            // 요구한 값과 부합하는지
        assertThat(auctionDTO.getAuctionBuyer()).isEqualTo("Buyer");
            // 예측한만큼 실행되었는지
        verify(auctionFeignClient, times(1)).findAuctionByBoardUuid(boardUUID);
    }
}