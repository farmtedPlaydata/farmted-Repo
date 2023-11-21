package com.farmted.passservice.service;

import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    String createAccessToken(String uuid, RoleEnums role);
    String createRefreshToken(String uuid, RoleEnums role);
    void saveRefreshToken(String uuid, String refreshToken);
    void setToken(String token, TokenType tokenType, HttpServletResponse response);
    void deleteCookie(HttpServletResponse response, HttpServletRequest request);
}
