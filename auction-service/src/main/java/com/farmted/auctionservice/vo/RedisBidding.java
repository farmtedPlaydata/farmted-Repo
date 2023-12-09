package com.farmted.auctionservice.vo;

import com.farmted.auctionservice.domain.TimeStamp;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder @Getter @AllArgsConstructor @NoArgsConstructor
@RedisHash(value = "auction", timeToLive = 3000)
public class RedisBidding extends TimeStamp {

    @Id
    private String biddingUuid;

    private Integer biddingPrice; // 일반 입찰
    private Integer biddingAutoPrice; // 자동 입찰
    private LocalDateTime biddingTime; // 응찰 시간

    private String memberUuid; // 응찰자
    private String boardUuid;

}
