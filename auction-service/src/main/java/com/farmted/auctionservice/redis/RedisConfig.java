package com.farmted.auctionservice.redis;

import com.farmted.auctionservice.domain.Bidding;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String dockerRedisHost;

    @Value("${spring.data.redis.port}")
    private int dockerRedisPort;

//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer(
//            RedisConnectionFactory connectionFactory){
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        return container;
//    }

//    @Bean
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        // Redis 호스트와 포트를 Docker에서 실행 중인 Redis의 정보로 설정
//        config.useSingleServer().setAddress("redis://" + dockerRedisHost + ":" + dockerRedisPort);
//        return Redisson.create(config);
//    }



    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(dockerRedisHost,dockerRedisPort);
        return new LettuceConnectionFactory(configuration);

    }


    @Bean
    public RedisTemplate<String,Long> redisTemplate(){

        // RedisTemplate 는 제네릭 타입 K,V 설정 가능
        // 첫 번째는 레디스 key 에 해당하고, 두 번째는 레디스 value 에 해당함
        RedisTemplate<String,Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // key 와 value 값을 직렬화/역직렬화하는 RedisSerializer 구현체 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }
}
