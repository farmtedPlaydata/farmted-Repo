package com.farmted.memberservice.dto.response;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.enums.RoleEnums;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberResponseDto {

    private String uuid;
    private String memberName;
    private RoleEnums role;

    public MemberResponseDto(Member member) {
        this.uuid = member.getMemberUuid();
        this.memberName = member.getMemberName();
        this.role = member.getMemberRole();
    }

    @QueryProjection
    public MemberResponseDto(String uuid, String memberName, RoleEnums role) {
        this.uuid = uuid;
        this.memberName = memberName;
        this.role = role;
    }
}
