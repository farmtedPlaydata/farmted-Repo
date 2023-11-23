package com.farmted.memberservice.dto.request;

import lombok.Getter;

@Getter
public class RequestUpdateMemberDto {
    private String memberName;
    private String memberAddress;
    private String memberAddressDetail;
    private String memberPhone;
}
