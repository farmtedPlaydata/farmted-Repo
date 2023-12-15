package com.farmted.boardservice.dto.response;

import com.farmted.boardservice.dto.response.listDomain.ResponseGetAuctionDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import com.farmted.boardservice.vo.PagingInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseGetCombinationListDto {
    private List<ResponseGetBoardDto> boardList;
    private PagingInfo page;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResponseGetProductDto> productList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResponseGetAuctionDto> auctionList;

    public void setPageList(Page<ResponseGetBoardDto> pageList, int currenPage) {
        this.boardList = pageList.getContent();
        this.page = PagingInfo.builder()
                        .pageNo(currenPage)
                        .totalPage(pageList.getTotalPages())
                        .build();
    }
}