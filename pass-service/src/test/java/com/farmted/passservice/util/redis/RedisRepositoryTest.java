//package com.farmted.passservice.util.redis;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//class RedisRepositoryTest {
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Test
//    @DisplayName("redis")
//    void RedisRepositoryTest() {
//        // given
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        String key = "TestKey";
//
//        // when
//        valueOperations.set(key, "Test");
//
//        // then
//        String value = valueOperations.get(key);
//        assertThat(value).isEqualTo("Test");
//
//    }
//
//}