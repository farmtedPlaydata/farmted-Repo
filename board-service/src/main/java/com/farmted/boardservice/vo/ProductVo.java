package com.farmted.boardservice.vo;


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
        @JsonProperty("boardUuid") String boardUuid
//        // 저장 시엔 필요없지만 요청 시엔 필요한 값
//        Boolean productStatus
    ){
    // %%% 테스트용 더미데이터 생성 메소드 (삭제 예정)
    public static ProductVo createDummyProduct(String boardUuid) {
        // 여기서 더미 데이터를 생성하고 반환
        return new ProductVo(
                "Dummy Product",
                10,
                10_000L,
                "Dummy Source",
                "Dummy Image",
                boardUuid
        );
    }
    // 값이 하나도 없을 경우(통신 이상) 대비
    public boolean isFilled() {
        return productName != null ||
                productStock != 0 ||
                productPrice != 0 ||
                productSource != null ||
                productImage != null ||
                boardUuid != null; //||
//                productStatus;
    }
}
