package com.farmted.boardservice.exception;

import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;

// 헤더를 통해 받아오는 RoleType에 대해 이상한 값이 존재하는 경우
public class RoleTypeException extends RuntimeException{
    // 헤더에 이상한 타입이 들어온 경우
    public RoleTypeException(String type) {
        super(type + " : 정해지지 않은 ROLE 타입입니다.");
    }
    // GUEST가 권한 밖의 요청을 신청한 경우
    public RoleTypeException(RoleEnums role, BoardType board) {
            super(role.getRoleKo()+"의 경우, "+ board.getTypeKo() +"에 대한 권한이 없습니다.");
    }
}
