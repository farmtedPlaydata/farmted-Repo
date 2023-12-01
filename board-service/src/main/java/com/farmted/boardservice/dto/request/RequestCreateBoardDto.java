package com.farmted.boardservice.dto.request;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.vo.ProductVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RequestCreateBoardDto(
        // 게시글용 정보
        @JsonProperty("boardType") BoardType boardType,
        @JsonProperty("boardContent") @NotBlank String boardContent,
        @JsonProperty("boardTitle") @NotBlank String boardTitle,
        // 아이템용 정보
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("productName") @NotBlank String productName,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("productStock") @Min(value = 1) int productStock,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("productPrice") @Min(value = 1) long productPrice,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("productSource") @NotBlank String productSource,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("productImage") @NotBlank String productImage) {

    // 게시글 전용 데이터 - 저장용 Entity
    public Board toBoard(String memberUuid, String memberName){
        return Board.builder()
                .memberUuid(memberUuid)
                .memberName(memberName)
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
                .boardType(this.boardType)
                .boardUuid(boardUuid)
                .build();
    }
}
