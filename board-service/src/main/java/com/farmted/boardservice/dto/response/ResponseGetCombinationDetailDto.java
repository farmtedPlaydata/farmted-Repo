package com.farmted.boardservice.dto.response;


import com.farmted.boardservice.dto.response.detailDomain.ResponseGetAuctionDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetBoardDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetProductDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetAuctionDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseGetCombinationDetailDto {
    private ResponseGetBoardDetailDto boardDetail;
    private ResponseGetProductDetailDto productDetail;
    private ResponseGetAuctionDetailDto auctionDetail;
}
