package com.farmted.memberservice.domain;

import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
import com.farmted.memberservice.enums.RoleEnums;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "members")
@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Member extends TimeStamp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    private String memberUuid;

    private String memberName;

    @Enumerated(value = EnumType.STRING)
    private RoleEnums memberRole;

    private String memberImage;

    private String memberAddress;

    private String memberAddressDetail;

    @Column(nullable = false, unique = true)
    private String memberPhone;

    private Boolean memberStatus;

    private Long memberBalance;

    public Member(String memberUuid, String memberName, RoleEnums memberRole, String memberPhone, Boolean memberStatus) {
        this.memberUuid = memberUuid;
        this.memberName = memberName;
        this.memberRole = memberRole;
        this.memberPhone = memberPhone;
        this.memberStatus = memberStatus;
    }

    public void updateMember(RequestUpdateMemberDto dto) {
        this.memberName = dto.getMemberName();
        this.memberAddress = dto.getMemberAddress();
        this.memberAddressDetail = dto.getMemberAddressDetail();
        this.memberPhone = dto.getMemberPhone();
    }

    public void changeRole(RoleEnums roles) {
        // 부여하는 권한을 가지고 있는 경우, 권한을 취소할 수 있음
        this.memberRole = this.memberRole == roles ? RoleEnums.USER : roles;
    }

}