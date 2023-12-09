package com.farmted.boardservice.exception;

import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.FeignDomainType;

// 특정 도메인과 Feign통신 중의 예외 발생
public class FeignException extends RuntimeException{
    // FeignException 강제 발생용
    public FeignException() {
        super();
    }
    // ResponseEntity가 null인 경우
    public FeignException(FeignDomainType domainType) {
        super(domainType.toString() +" 통신 중 Null이 반환되었습니다.");
    }
    // 반환받은 ResponseEntity를 GlobalReseponseDto로 변환 중 오류
    public FeignException(FeignDomainType domainType, ExceptionType exceptionType) {
        super(domainType.toString() +" 통신 중"+ exceptionType.toString());
    }
}