package com.farmted.auctionservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
public class Auction extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="auction_id")
    private Long auction_id;

    @Column(unique = true)
    @NotNull
    private String auctionUuid;

    @PositiveOrZero
    private BigDecimal auctionPrice; // 가격

    private String auctionBuyer;  // 낙찰자

    @NotNull
    private LocalDateTime auctionDeadline; // 경매 종료 시간

    @NotNull
    private Boolean auctionStatus; // false 0: 경매중, true 1:  경매 종료

    @NotNull
    private String memberUuid; // 판매자

    @NotNull
    private String boardUuid;

    @NotNull
    private String productUuid;

    @PrePersist
    public void createUuid(){
        auctionUuid = UUID.randomUUID().toString();
        auctionStatus = false; // false: 경매중, true:  경매 종료
    }

    public void setAuctionDeadlineForStatus(){
        auctionStatus = true;
    }

    public void setBiddingTop(BigDecimal auctionPrice,String auctionBuyer){
        this.auctionPrice=auctionPrice;
        this.auctionBuyer =auctionBuyer;
    }

}
