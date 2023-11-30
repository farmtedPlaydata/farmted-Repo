package com.farmted.passservice.service;

import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.TokenType;


public interface TokenService {
    String createToken(String uuid, RoleEnums role, TokenType tokenType);
    void saveRefreshToken(String uuid, String refreshToken);
    void logoutRefreshToken(String uuid, String refreshToken);
}
