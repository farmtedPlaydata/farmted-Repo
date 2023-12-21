package com.farmted.passservice.dto.request;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.enums.RoleEnums;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

@Getter @Setter
public class RequestCreatePassDto {

    @NotBlank @Email
    private String email;
    @NotBlank @Length(min = 4, max = 20)
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