package com.farmted.boardservice.dto.request;

import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.vo.ProductVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// 업데이트 로직이 사용하는 DTO
// 어차피 경매가 진행 중이라면 업데이트 불가능하므로
// 1. BoardType 을 경매 <-> 즉시 판매로 변경 가능해야함.
// 2. 경매가 진행 중이 아닌 경우(즉시판매, 종료 되었음에도 낙찰되지 않은 경매)엔 가격 변경 가능해야함
public record RequestUpdateProductBoardDto(
        // 게시글용 정보
        @JsonProperty("boardType") BoardType boardType,
        @JsonProperty("boardContent") @NotBlank String boardContent,
        @JsonProperty("boardTitle") @NotBlank String boardTitle,
        // 아이템용 정보
        @JsonProperty("productName") @NotBlank String productName,
        @JsonProperty("productStock") @Min(value = 1) int productStock,
        @JsonProperty("productPrice") @Min(value = 1) long productPrice,
        @JsonProperty("productSource") @NotBlank String productSource,
        @JsonProperty("productImage") @NotBlank String productImage
){
    // 상품 전용 데이터 - 전송용 VO
    public ProductVo toProduct(String boardUuid){
        return ProductVo.builder()
                .productName(this.productName)
                .productStock(this.productStock)
                .productPrice(this.productPrice)
                .productSource(this.productSource)
                .productImage(this.productImage)
                .boardUuid(boardUuid)
                .build();
    }
}
