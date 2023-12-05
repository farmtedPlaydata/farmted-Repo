package com.farmted.memberservice.dto.request;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class RequestCreateMemberDto {
    private RoleEnums memberRole;
    private Boolean memberStatus;
    private String memberName;
    private String memberAddress;
    private String memberAddressDetail;
    private String memberPhone;
    private String uuid;

    public Member toEntity() {
        return Member.builder()
                .memberUuid(this.uuid)
                .memberAddress(this.memberAddress)
                .memberName(this.memberName)
                .memberBalance(10000L)
                .memberAddressDetail(this.memberAddressDetail)
                .memberPhone(this.memberPhone)
                .memberRole(RoleEnums.USER)
                .memberStatus(true)
                .build();
    }
}
