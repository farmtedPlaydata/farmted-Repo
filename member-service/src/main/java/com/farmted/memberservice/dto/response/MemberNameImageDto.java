package com.farmted.memberservice.dto.response;

import com.farmted.memberservice.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberNameImageDto {

    private String memberName;
    private String memberProfileImage;

    @Builder
    public MemberNameImageDto(String memberName, String memberProfileImage) {
        this.memberName = memberName;
        this.memberProfileImage = memberProfileImage;
    }
}
