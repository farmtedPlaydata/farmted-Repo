package com.farmted.authservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnums {
    GUEST("ROLE_GUEST");

    private final String key;

}
