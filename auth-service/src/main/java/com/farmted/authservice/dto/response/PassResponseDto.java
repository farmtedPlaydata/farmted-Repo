package com.farmted.authservice.dto.response;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PassResponseDto {

    private String email;
    private String uuid;

    public PassResponseDto(Pass pass) {
        this.email = pass.getEmail();
        this.uuid = pass.getUuid();
    }

}
