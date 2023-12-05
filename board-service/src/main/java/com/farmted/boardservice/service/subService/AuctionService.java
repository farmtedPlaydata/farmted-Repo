package com.farmted.boardservice.service.subService;

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

    // 판매자의 낙찰 내역 조회
    public List<ResponseGetAuctionDto> getSellerAuctionList(String memberUuid){
        return auctionFeignClient.findAuctionToSeller(memberUuid)
                .getBody().stream().map(ResponseGetAuctionDto::new)
                .toList();
    }

    // 구매자의 낙찰 내역 조회
    public List<ResponseGetAuctionDto> getBuyerAuctionList(String memberUuid){
        return auctionFeignClient.findAuctionTrue(memberUuid)
                .getBody().stream().map(ResponseGetAuctionDto::new)
                .toList();
    }
}
