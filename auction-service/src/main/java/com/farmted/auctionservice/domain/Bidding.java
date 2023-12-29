package com.farmted.auctionservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Builder @Getter
public class Bidding extends TimeStamp {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long biddingId;
    @NotNull
    private String biddingUuid;

    private BigDecimal biddingPrice; // 일반 입찰
    private Integer biddingAutoPrice; // 자동 입찰

    private LocalDateTime biddingTime; // 응찰 시간
    private String memberUuid; // 응찰자

    private Integer memberPrice; // 응찰자 잔고
    private String boardUuid;

    @PrePersist
    public void createUuid(){
        biddingUuid = UUID.randomUUID().toString();
    }

}
