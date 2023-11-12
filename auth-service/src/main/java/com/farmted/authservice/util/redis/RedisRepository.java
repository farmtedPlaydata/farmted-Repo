package com.farmted.authservice.util.redis;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RefreshToken, String> {
}
