package com.farmted.boardservice.dto.response;


import com.farmted.boardservice.dto.response.detailDomain.ResponseGetAuctionDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetBoardDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetProductDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseGetCombinationDetailDto {
    private ResponseGetBoardDetailDto boardDetail;
    private ResponseGetProductDetailDto productDetail;
    private ResponseGetAuctionDetailDto auctionDetail;
}
