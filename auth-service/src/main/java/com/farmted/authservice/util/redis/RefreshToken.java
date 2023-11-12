package com.farmted.authservice.util.redis;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(timeToLive = 14 * 24 * 60 * 60 * 1000L)  // 2주
@Builder
public class RefreshToken {

    @Id
    private String uuid;
    private String refreshToken;

    @TimeToLive
    private Long expiration;

    public void updateToken(String refreshToken, Long expiration) {
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
