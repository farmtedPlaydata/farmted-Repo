package com.farmted.boardservice.enums;

import com.farmted.boardservice.exception.RoleTypeException;
import lombok.Getter;

@Getter
public enum RoleEnums {
    GUEST("ROLE_GUEST", "게스트"),
    USER("ROLE_USER", "유저"),
    ADMIN("ROLE_ADMIN", "관리자");

    private String roleEn;
    private String roleKo;

    RoleEnums(String roleEn, String roleKo) {
        this.roleEn = roleEn;
        this.roleKo = roleKo;
    }

    // 요청한 클라이언트의 ROLE 확인
    public static RoleEnums roleCheck(String role){
        for(RoleEnums roleType : RoleEnums.values()){
            if(roleType.getRoleEn().equals(role)){
                return roleType;
            }
        }
        throw new RoleTypeException(role);
    }
}
