package com.farmted.boardservice.exception;

public class RoleTypeException extends RuntimeException{
    // 헤더에 이상한 타입이 들어온 경우
    public RoleTypeException(String type) {
        super(type + " : 정해지지 않은 ROLE 타입입니다.");
    }
    // GUEST가 권한 밖의 요청을 신청한 경우
    public RoleTypeException() {
            super("GUEST는 해당 작업을 수행할 수 없습니다.");
    }
}
