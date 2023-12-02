package com.farmted.memberservice.dto.request;

import com.farmted.memberservice.enums.RoleEnums;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchMemberDto extends PagingDto {
    private Boolean status;
    private String uuid;
    private String memberName;
    private RoleEnums memberRole;
}
