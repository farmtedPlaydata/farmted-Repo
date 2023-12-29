package com.farmted.memberservice.dto.response;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.enums.RoleEnums;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberResponseDto {

    private String memberUuid;
    private String memberName;
    private RoleEnums memberRole;

    public MemberResponseDto(Member member) {
        this.memberUuid = member.getMemberUuid();
        this.memberName = member.getMemberName();
        this.memberRole = member.getMemberRole();
    }

    @QueryProjection
    public MemberResponseDto(String memberUuid, String memberName, RoleEnums memberRole) {
        this.memberUuid = memberUuid;
        this.memberName = memberName;
        this.memberRole = memberRole;
    }
}
