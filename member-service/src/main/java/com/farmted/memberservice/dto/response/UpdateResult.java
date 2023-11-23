package com.farmted.memberservice.dto.response;

import com.farmted.memberservice.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateResult {
    private String message;
    private Member updateMember;

    public UpdateResult(String message, Member updateMember) {
        this.message = message;
        this.updateMember = updateMember;
    }
}
