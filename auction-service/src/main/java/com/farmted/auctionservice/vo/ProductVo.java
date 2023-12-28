package com.farmted.auctionservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductVo(
    @JsonProperty("name") String name,
    @JsonProperty("stock") int stock,
    @JsonProperty("source") String source,
    @JsonProperty("image") String image
){}
