package com.farmted.boardservice.dto.request;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.vo.ProductVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RequestCreateBoardDto(
        // 게시글용 정보
        @JsonProperty("boardType") BoardType boardType,
        @JsonProperty("boardContent") @NotBlank String boardContent,
        @JsonProperty("boardTitle") @NotBlank String boardTitle,
        // 아이템용 정보
        @JsonProperty("productName") @NotBlank String productName,
        @JsonProperty("productStock") @Min(value = 1) int productStock,
        @JsonProperty("productPrice") @Min(value = 1) long productPrice,
        @JsonProperty("productSource") @NotBlank String productSource,
        @JsonProperty("productImage") @NotBlank String productImage) {

    // 게시글 전용 데이터 - 저장용 Entity
    public Board toBoard(String memberUuid){
        return Board.builder()
                .memberUuid(memberUuid)
                .boardType(this.boardType)
                .boardTitle(this.boardTitle)
                .boardContent(this.boardContent)
                .build();
    }

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
