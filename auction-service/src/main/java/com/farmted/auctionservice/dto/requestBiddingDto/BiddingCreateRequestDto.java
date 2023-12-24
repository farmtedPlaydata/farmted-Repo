package com.farmted.auctionservice.dto.requestBiddingDto;

import com.farmted.auctionservice.domain.Bidding;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @AllArgsConstructor @NoArgsConstructor @Builder @Setter
public class BiddingCreateRequestDto {

    @NotNull(message = "입찰 희망 가격은 필수 입력값 입니다.")
    @DecimalMin(value = "0", inclusive = true, message = "입찰 희망 가격은 0원 이상이어야 합니다.")
    private BigDecimal biddingPrice;

    private Integer memberPrice; // 사용자 잔고
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer biddingAutoPrice; // 자동 입찰

    public Bidding toEntity(String boardUuid,String memberUuid){
        return Bidding.builder()
                .biddingPrice(biddingPrice)
                .boardUuid(boardUuid)
                .memberUuid(memberUuid)
                .biddingTime(LocalDateTime.now())
                .biddingUuid(UUID.randomUUID().toString())
                .build();
    }


}
