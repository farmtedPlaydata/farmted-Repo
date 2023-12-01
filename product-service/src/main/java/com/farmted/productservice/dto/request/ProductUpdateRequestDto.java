package com.farmted.productservice.dto.request;

// 전체 수정 시
public record ProductUpdateRequestDto (
    String name,
     int stock,
    int price,
     String source,
     String image
){}
