package com.farmted.passservice.dto.response;

import com.farmted.passservice.domain.Pass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ResponseListDto {

    private String email;
    private String uuid;

    public ResponseListDto (Pass pass) {
        this.email = pass.getEmail();
        this.uuid = pass.getUuid();
    }
}
