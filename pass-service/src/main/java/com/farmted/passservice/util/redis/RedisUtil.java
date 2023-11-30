package com.farmted.passservice.util.redis;

public interface RedisUtil {
    void updateToken(String uuid, String refreshToken, Long expiration);
}
