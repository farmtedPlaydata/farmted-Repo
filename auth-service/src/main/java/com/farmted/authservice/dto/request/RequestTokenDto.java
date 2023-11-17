package com.farmted.authservice.dto.request;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestTokenDto {

    private String uuid;
    private RoleEnums role;


}
