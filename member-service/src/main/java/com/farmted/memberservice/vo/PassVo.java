package com.farmted.memberservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PassVo {

    @JsonProperty("memberUuid")
    private String memberUuid;
    @JsonProperty("memberRole")
    private String memberRole;
    @JsonProperty("email")
    private String email;

}
