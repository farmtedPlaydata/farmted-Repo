package com.farmted.memberservice.service;

import com.farmted.memberservice.dto.request.RequestCreateMemberDto;

public interface MemberService {
    void createMember(RequestCreateMemberDto dto);
}
