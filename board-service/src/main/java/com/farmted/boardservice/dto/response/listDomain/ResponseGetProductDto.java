package com.farmted.boardservice.dto.response.listDomain;

import com.farmted.boardservice.vo.ProductVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ResponseGetProductDto {
    @JsonProperty("productName")
    String productName;
    @JsonProperty("productSource")
    String productSource;
    @JsonProperty("productImage")
    String productImage;
    @JsonProperty("productPrice")
    Long productPrice;

    // 경매용 정보 (null 이면 직렬화 대상에서 제외)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("auctionPrice")
    Integer auctionPrice;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("auctionDeadline")
    LocalDateTime auctionDeadline;

    public ResponseGetProductDto(ProductVo productVo){
        // 상품용 정보
        this.productName = productVo.productName();
        this.productSource = productVo.productSource();
        this.productImage = productVo.productImage();
        this.productPrice = productVo.productPrice();
        this.auctionPrice = (productVo.auctionPrice()!=null)
                                ? productVo.auctionPrice()
                                : null;
        this.auctionDeadline = (productVo.auctionDeadline()!=null)
                                ? productVo.auctionDeadline()
                                : null;
    }
}
