package com.farmted.memberservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnums {
<<<<<<< Updated upstream
    GUEST("ROLE_GUEST"),
=======
>>>>>>> Stashed changes
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String key;
}
