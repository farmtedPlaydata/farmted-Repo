package com.farmted.boardservice.dto.response.detailDomain;

import com.farmted.boardservice.vo.ProductVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ResponseGetProductDetailDto {
    // MS별로 따로 반환한다면 필요없는 데이터
    // 아이템용 정보
    private String productName;
    private int productStock;
    // 최저가는 경매 정보엔 필요없을 것이라 판단해서 제외
    private String productSource;
    private String productImage;
    // 상품 상태의 경우, GET 요청엔 필요하다 판단해서 추가
    private boolean productStatus;

    public ResponseGetProductDetailDto(ProductVo productVo) {
        this.productName = productVo.productName();
        this.productStock = productVo.productStock();
        this.productSource = productVo.productSource();
        this.productImage = productVo.productImage();
        this.productStatus = productVo.productStatus();
    }
}
