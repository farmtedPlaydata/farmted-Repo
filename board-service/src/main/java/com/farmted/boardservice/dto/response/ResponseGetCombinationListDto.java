package com.farmted.boardservice.dto.response;

import com.farmted.boardservice.dto.response.listDomain.ResponseGetAuctionDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResponseGetProductDto> productList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResponseGetAuctionDto> auctionList;
}