package com.farmted.auctionservice.dto.requestAuctionDto;

import com.farmted.auctionservice.domain.Auction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class AuctionCreateRequestDto {

    @NotNull(message = "상품 가격은 필수 입력값 입니다.")
    @DecimalMin(value = "0", inclusive = true, message = "상품 가격은 0 이상이어야 합니다.")
    private BigDecimal auctionPrice; // 가격
    private LocalDateTime auctionDeadline; // 경매 종료 시간
    private String boardUuid;
    private String productUuid;


    public Auction toEntity(String memberUuid,LocalDateTime actionDeadline){
        return Auction.builder()
                .auctionPrice(this.auctionPrice)
                .auctionDeadline(actionDeadline)
                .memberUuid(memberUuid)
                .boardUuid(this.boardUuid)
                .productUuid(this.productUuid)
                .build();
    }
}
