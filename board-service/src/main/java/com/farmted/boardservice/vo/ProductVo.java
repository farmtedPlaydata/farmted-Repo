package com.farmted.boardservice.vo;


import com.farmted.boardservice.enums.BoardType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductVo (
        @JsonProperty("name") String productName,
        @JsonProperty("stock") @NotBlank Integer productStock,
        @JsonProperty("price") @NotBlank Long productPrice,
        @JsonProperty("source") String productSource,
        @JsonProperty("image") String productImage,
        @JsonProperty("status") Boolean productStatus,

        // 여기부터 요청할때만 필요한 값
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("boardUuid") String boardUuid,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("boardType") BoardType boardType,

                // 리스트 조회시 필요한 일부의 경매 데이터
        @JsonInclude(JsonInclude.Include.NON_NULL) @JsonProperty("auctionPrice") Integer auctionPrice,
        @JsonInclude(JsonInclude.Include.NON_NULL) @JsonProperty("auctionDeadline") LocalDateTime auctionDeadline
        ){
}