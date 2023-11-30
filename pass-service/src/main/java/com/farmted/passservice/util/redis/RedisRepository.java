package com.farmted.passservice.util.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisRepository extends CrudRepository<RefreshToken, String> {
    void deleteRefreshTokenByUuid(String uuid);
}
