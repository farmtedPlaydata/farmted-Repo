package com.farmted.passservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RequestLoginDto {
    private String email;
    private String password;

    @Builder
    public RequestLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
