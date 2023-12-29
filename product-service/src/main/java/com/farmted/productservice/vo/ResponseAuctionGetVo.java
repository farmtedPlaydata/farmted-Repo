package com.farmted.productservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;


public record ResponseAuctionGetVo (
        @JsonProperty("auctionPrice") Integer auctionPrice, // 경매 가격 == 낙찰 가격
        @JsonProperty("auctionDeadline") LocalDateTime auctionDeadline, // 경매 종료
        @JsonProperty("productUuid") String productUuid,
        @JsonProperty("auctionBuyer") String auctionBuyer,  // 낙찰자
        @JsonProperty("auctionStatus") boolean auctionStatus){ }
