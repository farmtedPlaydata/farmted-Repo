package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.dto.response.detailDomain.ResponseGetAuctionDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetAuctionDto;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.FeignDomainType;
import com.farmted.boardservice.feignClient.AuctionFeignClient;
import com.farmted.boardservice.util.feignConverter.FeignConverter;
import com.farmted.boardservice.vo.AuctionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionFeignClient auctionFeignClient;
    private final FeignConverter<AuctionVo> auctionConverter;

    // 경매에 대한 페이징 리스트
    public List<ResponseGetAuctionDto> getAuctionList(int pageNo) {
        return auctionConverter.convertListVo(auctionFeignClient.findAuctionList(pageNo),
                    FeignDomainType.AUCTION, ExceptionType.GETLIST)
                .stream().map(ResponseGetAuctionDto::new)
                .toList();
    }

    // 판매자의 경매 내역 조회
    public List<ResponseGetAuctionDto> getSellerAuctionList(String memberUuid, int pageNo){
        return auctionConverter.convertListVo(auctionFeignClient.findAuctionToSeller(memberUuid, pageNo),
                        FeignDomainType.AUCTION, ExceptionType.GETLIST)
                .stream().map(ResponseGetAuctionDto::new)
                .toList();
    }

    // 경매 상세 조회
    public ResponseGetAuctionDetailDto getAuctionDetail(String boardUuid){
        return new ResponseGetAuctionDetailDto(
                auctionConverter.convertSingleVo(
                        auctionFeignClient.findAuctionByBoardUuid(boardUuid),
                            FeignDomainType.AUCTION, ExceptionType.GET));
    }

}
