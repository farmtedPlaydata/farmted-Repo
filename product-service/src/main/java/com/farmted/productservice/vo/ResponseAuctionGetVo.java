package com.farmted.productservice.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


public record ResponseAuctionGetVo (
    Integer auctionPrice, // 경매 가격
    LocalDate auctionDeadline, // 경매 종료
    String productUuid,
    boolean auctionStatus){ }
