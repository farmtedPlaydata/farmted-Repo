package com.farmted.boardservice.exception;

import com.farmted.boardservice.enums.ExceptionType;

// 상품과의 Feign통신 중의 예외
public class ProductFeignException extends RuntimeException{
    // 상품 저장 중 오류 발생
    public ProductFeignException(ExceptionType type) {
        super("상품" + type.toString());
    }
}
