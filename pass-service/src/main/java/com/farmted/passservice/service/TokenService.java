package com.farmted.passservice.service;

import com.farmted.passservice.enums.RoleEnums;

public interface TokenService {
    String createAccessToken(String uuid, RoleEnums role);
    String createRefreshToken(String uuid, RoleEnums role);
    void saveRefreshToken(String uuid, String refreshToken);
}
