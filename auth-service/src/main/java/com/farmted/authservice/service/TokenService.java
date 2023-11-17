package com.farmted.authservice.service;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.enums.RoleEnums;

import java.util.Optional;

public interface TokenService {
    String createAccessToken(String uuid, RoleEnums role);
    String createRefreshToken(String uuid, RoleEnums role);
    void saveRefreshToken(String uuid, String refreshToken);
}
