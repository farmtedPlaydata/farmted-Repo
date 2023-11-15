package com.farmted.boardservice.vo;


import lombok.Builder;

@Builder
public record ProductVo (
        String productName,
        int productStock,
        long productPrice,
        String productSource,
        String productImage,
        String boardUuid,
        // 저장 시엔 필요없지만 요청 시엔 필요한 값
        boolean productStatus
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
                boardUuid,
                true
        );
    }
}
