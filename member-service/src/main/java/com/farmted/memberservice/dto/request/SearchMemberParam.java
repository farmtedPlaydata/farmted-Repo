package com.farmted.memberservice.dto.request;

import com.farmted.memberservice.enums.RoleEnums;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SearchMemberParam {
    private String memberName;
    private RoleEnums memberRole;
    private Boolean memberStatus;

    public static SearchMemberParam valueOf(SearchMemberDto dto) {
        return SearchMemberParam.builder()
                .memberStatus(dto.getMemberStatus())
                .memberName(dto.getMemberName())
                .memberRole(dto.getMemberRole())
                .build();
    }
}
