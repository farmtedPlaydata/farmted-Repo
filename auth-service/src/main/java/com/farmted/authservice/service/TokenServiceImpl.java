package com.farmted.authservice.service;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.enums.RoleEnums;
import com.farmted.authservice.enums.TokenType;
import com.farmted.authservice.util.jwt.JwtProdiver;
import com.farmted.authservice.util.redis.RedisRepository;
import com.farmted.authservice.util.redis.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtProdiver jwtProdiver;
    private final RedisRepository redisRepository;

    public String createAccessToken(String uuid, RoleEnums role) {
        return jwtProdiver.createToken(uuid, role, TokenType.ACCESS);
    }

    @Override
    public String createRefreshToken(String uuid, RoleEnums role) {
        return jwtProdiver.createToken(uuid, role, TokenType.REFRESH);
    }

    @Override
    public void saveRefreshToken(String uuid, String refreshToken) {
        redisRepository.findById(uuid)
                .ifPresentOrElse(
                        refreshTokenEntity -> {
                            refreshTokenEntity.updateToken(refreshToken, JwtProdiver.REFRESH_TOKEN_TIME);
                            redisRepository.save(refreshTokenEntity);
                        },
                        () -> {
                            RefreshToken refreshToSave = RefreshToken.builder()
                                    .uuid(uuid)
                                    .refreshToken(refreshToken)
                                    .expiration(JwtProdiver.REFRESH_TOKEN_TIME)
                                    .build();
                            redisRepository.save(refreshToSave);
                        }
                );
    }
}
