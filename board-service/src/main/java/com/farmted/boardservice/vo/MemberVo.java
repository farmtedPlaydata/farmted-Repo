package com.farmted.boardservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberVo(
        @JsonProperty("memberName") String memberName,
        @JsonProperty("memberImage") String memberProfile
) {
}