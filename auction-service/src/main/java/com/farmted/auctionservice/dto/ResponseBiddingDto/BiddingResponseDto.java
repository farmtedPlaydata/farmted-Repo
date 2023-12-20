package com.farmted.auctionservice.dto.ResponseBiddingDto;

import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.vo.ProductVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor @NoArgsConstructor
public class BiddingResponseDto {
    private BigDecimal biddingPrice; // 일반 입찰 금액
    private LocalDateTime biddingTime; // 입찰 시간
    private String memberUuid; // 응찰자
    private String name;
    private int stock;
    private String source;
    private String image;

    public BiddingResponseDto(Bidding bidding, ProductVo productVo){
        biddingPrice = bidding.getBiddingPrice();
        biddingTime=bidding.getBiddingTime();
        memberUuid = bidding.getMemberUuid();
        name = productVo.name();
        stock = productVo.stock();
        source=productVo.source();
        image = productVo.image();
    }
}
