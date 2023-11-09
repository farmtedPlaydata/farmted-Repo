package com.farmted.authservice.enums;

import lombok.Getter;

@Getter
public enum RoleEnums {
    GUEST(Authority.GUEST, "게스트"),
    USER(Authority.USER, "유저"),
    SELLER(Authority.SELLER, "판매자"),
    ADMIN(Authority.ADMIN, "관리자");

    private final String authority;
    private final String people;

    RoleEnums(String authority, String people) {
        this.authority = authority;
        this.people = people;
    }

    public static class Authority {
        public static final String GUEST = "ROLE_GUEST";
        public static final String USER = "ROLE_USER";
        public static final String SELLER = "ROLE_SELLER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
