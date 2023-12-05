package com.farmted.memberservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PassVo {
    @JsonProperty("email")
    private String email;
    @JsonProperty("uuid")
    private String uuid;
}
