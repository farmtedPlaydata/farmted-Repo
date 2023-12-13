package com.farmted.passservice.dto.request;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class RequestCreatePassDto {

    private String email;
    private String password;
    private RoleEnums role;
    private Boolean status;
    private String uuid;

    public Pass toEntity() {
        return Pass.builder()
                .email(this.email)
                .password(this.password)
                .uuid(UUID.randomUUID().toString())
                .role(RoleEnums.GUEST)
                .status(false)
                .build();
    }
}