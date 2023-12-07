package com.farmted.boardservice.dto.response.detailDomain;

import com.farmted.boardservice.vo.ProductVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor @AllArgsConstructor
public class ResponseGetProductDetailDto {
    // MS별로 따로 반환한다면 필요없는 데이터
    // 아이템용 정보
    private String productName;
    private Integer productStock;
    private Long productPrice;
    private String productSource;
    private String productImage;

    public ResponseGetProductDetailDto(ProductVo productVo) {
        this.productName = productVo.productName();
        this.productStock = productVo.productStock();
        this.productPrice = productVo.productPrice();
        this.productSource = productVo.productSource();
        this.productImage = productVo.productImage();
    }
}
