package com.farmted.passservice.service;

import com.farmted.passservice.enums.RoleEnums;
import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.util.jwt.JwtProvider;
import com.farmted.passservice.util.redis.RedisRepository;
import com.farmted.passservice.util.redis.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtProvider jwtProvider;
    private final RedisRepository redisRepository;

    public String createToken(String uuid, RoleEnums role, TokenType tokenType) {
        return jwtProvider.createToken(uuid, role, tokenType);
    }

    @Override
    public void saveRefreshToken(String uuid, String refreshToken) {
        redisRepository.findById(uuid)
                .ifPresentOrElse(
                        refreshTokenEntity -> {
                            refreshTokenEntity.updateToken(uuid, refreshToken, JwtProvider.REFRESH_TOKEN_TIME);
                            redisRepository.save(refreshTokenEntity);
                        },
                        () -> {
                            RefreshToken refreshToSave = RefreshToken.builder()
                                    .uuid(uuid)
                                    .refreshToken(refreshToken)
                                    .expiration(JwtProvider.REFRESH_TOKEN_TIME)
                                    .build();
                            redisRepository.save(refreshToSave);
                        }
                );
    }
}
