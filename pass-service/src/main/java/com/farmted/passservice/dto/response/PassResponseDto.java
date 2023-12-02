package com.farmted.passservice.dto.response;

import com.farmted.passservice.domain.Pass;
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
