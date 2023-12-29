package com.farmted.memberservice.dto.request;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.enums.RoleEnums;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class RequestCreateMemberDto {
    private RoleEnums memberRole;
    private Boolean memberStatus;
    @NotBlank
    private String memberName;
    private String memberAddress;
    private String memberAddressDetail;
    @NotBlank
    private String memberPhone;
    private String memberUuid;
    private String memberProfile;
    private String email;

    public Member toEntity() {
        return Member.builder()
                .memberUuid(this.memberUuid)
                .memberAddress(this.memberAddress)
                .memberName(this.memberName)
                .memberBalance(10000L)
                .memberAddressDetail(this.memberAddressDetail)
                .memberPhone(this.memberPhone)
                .memberRole(RoleEnums.USER)
                .memberProfile(this.memberProfile)
                .memberStatus(true)
                .build();
    }
}
