package com.farmted.memberservice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter @Setter
@NoArgsConstructor
public class ResponsePagingToListDto {

    Page<MemberResponseDto> memberList;
}
