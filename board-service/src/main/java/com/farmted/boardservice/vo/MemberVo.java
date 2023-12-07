package com.farmted.boardservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberVo(
        @JsonProperty("name") String memberName,
        @JsonProperty("profile") String memberProfile
) {
}
