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
    private String memberAddress;
    private String memberAddressDetail;
    private String memberPhone;

    public Member toEntity() {
        return Member.builder()
                .memberUuid(UUID.randomUUID().toString())
                .memberAddress(this.memberAddress)
                .memberAddressDetail(this.memberAddressDetail)
                .memberPhone(this.memberPhone)
                .memberRole(this.memberRole)
                .memberStatus(true)
                .build();
    }
}
