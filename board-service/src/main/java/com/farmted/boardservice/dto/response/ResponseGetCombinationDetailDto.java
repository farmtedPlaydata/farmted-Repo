package com.farmted.boardservice.dto.response;


import com.farmted.boardservice.dto.response.detailDomain.ResponseGetAuctionDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetBoardDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetProductDetailDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseGetCombinationDetailDto {
    private ResponseGetBoardDetailDto boardDetail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponseGetProductDetailDto productDetail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponseGetAuctionDetailDto auctionDetail;
}
