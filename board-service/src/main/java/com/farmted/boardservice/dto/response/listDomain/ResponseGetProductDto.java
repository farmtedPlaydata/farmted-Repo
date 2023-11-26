package com.farmted.boardservice.dto.response.listDomain;

import com.farmted.boardservice.vo.ProductVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class ResponseGetProductDto {
    @JsonProperty("name")
    String productName;
    @JsonProperty("source")
    String productSource;
    @JsonProperty("image")
    String productImage;
    public ResponseGetProductDto(ProductVo productVo){
        // 상품용 정보
        this.productName = productVo.productName();
        this.productSource = productVo.productSource();
        this.productImage = productVo.productImage();
    }
}
