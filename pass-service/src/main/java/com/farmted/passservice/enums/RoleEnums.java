package com.farmted.passservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnums {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MASTER("ROLE_MASTER");

    private final String key;

    @JsonCreator
    public static RoleEnums fromKey(String key) {
        for (RoleEnums roleEnum : RoleEnums.values()) {
            if (roleEnum.getKey().equals(key)) {
                return roleEnum;
            }
        }
        throw new IllegalArgumentException("Unknown key: " + key);
    }
}
