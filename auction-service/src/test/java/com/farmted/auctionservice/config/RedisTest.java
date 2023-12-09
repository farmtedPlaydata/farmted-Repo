package com.farmted.auctionservice.config;

import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.service.BiddingService;
import com.farmted.auctionservice.vo.RedisBidding;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
class RedisTest {

    @Autowired private RedisTemplate<String,RedisBidding> redisTemplate;

    @Test
    public void redisSave(){
        // given
        RedisBidding bidding = RedisBidding.builder()
                .biddingUuid(UUID.randomUUID().toString())
                .biddingPrice(1000)
                .biddingAutoPrice(4000)
                .biddingTime(LocalDateTime.now())
                .memberUuid("123M")
                .boardUuid("123B")
                .build();

        // when
        redisTemplate.opsForValue().set(bidding.getBiddingUuid(), bidding);

        //then


    }



}