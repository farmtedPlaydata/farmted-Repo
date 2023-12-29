package com.farmted.passservice.util.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@Getter
@RedisHash(timeToLive = 14 * 24 * 60 * 60)
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RefreshToken implements Serializable {

    @Id
    private String uuid;

    private String refreshToken;

    @TimeToLive
    private Long expiration;


    public void updateToken(String uuid, String refreshToken, Long expiration) {
        this.uuid = uuid;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
