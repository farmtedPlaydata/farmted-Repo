package com.farmted.memberservice.domain;

import com.farmted.memberservice.enums.MemberRoleEnums;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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

    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnums memberRole;

    private String memberImage;

    private String memberAddress;

    private String memberAddressDetail;

    private String memberPhone;

    private Boolean memberStatus;

    private Long memberBalance;
}