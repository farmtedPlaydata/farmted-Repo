package com.farmted.boardservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PagingInfo(
        @JsonProperty("pageNo") int pageNo,
        @JsonProperty("totalPage") int totalPage,
        @JsonProperty("totalElements") long totalElements
) {
}
