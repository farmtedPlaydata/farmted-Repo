package com.farmted.memberservice.service;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    void createMember(RequestCreateMemberDto dto);

    void updateMember(RequestUpdateMemberDto dto, Member member);
}
