package com.farmted.passservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberVo {

    @JsonProperty("memberUuid")
    private String memberUuid;
    @JsonProperty("memberRole")
    private String memberRole;
    @JsonProperty("email")
    private String email;
}
