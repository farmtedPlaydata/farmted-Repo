package com.farmted.passservice.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisUtilImpl implements RedisUtil{

    private final RedisTemplate<String, Object> redisTemplate;

    public void updateToken(String uuid, String refreshToken, Long expiration) {
        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.updateToken(uuid, refreshToken, expiration);

        String key = "RefreshToken:" + uuid;
        redisTemplate.opsForValue().set(key, refreshTokenObj);

        if (expiration != null && expiration > 0) {
            redisTemplate.expire(key, expiration, TimeUnit.MILLISECONDS);
        }
    }
}
