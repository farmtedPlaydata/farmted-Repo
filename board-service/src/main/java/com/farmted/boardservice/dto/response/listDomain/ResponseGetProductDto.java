package com.farmted.boardservice.dto.response.listDomain;

import com.farmted.boardservice.vo.ProductVo;

public class ResponseGetProductDto {
    // 상품용 정보
    private String productName;
    private String productSource;
    private String productImage;

    public ResponseGetProductDto(ProductVo productVo){
        this.productName = productVo.productName();
        this.productSource = productVo.productSource();
        this.productImage = productVo.productImage();
    }
}
