package com.farmted.boardservice.dto.response;

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
public class ResponseGetCombinationListDto {
    private Page<ResponseGetBoardDto> boardList;
    private List<ResponseGetProductDto> productList;
    private List<ResponseGetAuctionDto> auctionList;
}