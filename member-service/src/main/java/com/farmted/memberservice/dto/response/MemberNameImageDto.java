package com.farmted.memberservice.dto.response;

import com.farmted.memberservice.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberNameImageDto {

    private String memberName;
    private String memberImage;

    @Builder
    public MemberNameImageDto(String memberName, String memberImage) {
        this.memberName = memberName;
        this.memberImage = memberImage;
    }
}
