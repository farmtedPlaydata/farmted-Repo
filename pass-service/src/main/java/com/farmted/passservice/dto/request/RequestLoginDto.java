package com.farmted.passservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
public class RequestLoginDto {
    @NotBlank @Email
    private String email;
    @NotBlank
    @Length(min = 4, max = 20)
    private String password;

    @Builder
    public RequestLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
