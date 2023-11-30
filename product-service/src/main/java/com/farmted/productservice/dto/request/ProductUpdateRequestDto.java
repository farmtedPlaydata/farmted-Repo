package com.farmted.productservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 전체 수정 시
@Getter @AllArgsConstructor @NoArgsConstructor
public class ProductUpdateRequestDto {
    private String name;

    private int stock;

    private int price;

    private String source;

    private String image;
}
