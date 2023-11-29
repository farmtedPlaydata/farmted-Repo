package com.farmted.boardservice.vo;


import com.farmted.boardservice.enums.BoardType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProductVo (
        @JsonProperty("name") String productName,
        @JsonProperty("stock") @NotBlank int productStock,
        @JsonProperty("price") @NotBlank long productPrice,
        @JsonProperty("source") String productSource,
        @JsonProperty("image") String productImage,
        @JsonProperty("boardUuid") String boardUuid,
        @JsonProperty("status") boolean productStatus,
        @JsonProperty("boardType") BoardType boardType
    ){
}
