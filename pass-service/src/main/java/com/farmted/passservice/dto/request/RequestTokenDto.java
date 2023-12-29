package com.farmted.passservice.dto.request;

import com.farmted.passservice.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestTokenDto {
    private String uuid;
    private RoleEnums role;

}
