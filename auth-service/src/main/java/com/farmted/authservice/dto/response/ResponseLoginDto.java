package com.farmted.authservice.dto.response;

import com.farmted.authservice.global.security.UserDetailsImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ResponseLoginDto {

    private String email;
    private String token;

    @Builder
    public ResponseLoginDto(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public static ResponseLoginDto fromEntity(UserDetailsImpl pass, String token) {
        return ResponseLoginDto.builder()
                // UserDetailsImpl의 getUsername은 email을 반환
                .email(pass.getUsername())
                .token(token)
                .build();
    }
}
