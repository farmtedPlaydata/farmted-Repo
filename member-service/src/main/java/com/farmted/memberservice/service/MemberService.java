package com.farmted.memberservice.service;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
import com.farmted.memberservice.enums.RoleEnums;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    // 회원 상세 정보
    void createMember(RequestCreateMemberDto dto);
    // 회원 정보 수정
    void updateMember(RequestUpdateMemberDto dto, Member member);
    // 회원 삭제
    void deleteMember(String uuid);
    void grantRole(String uuid, RoleEnums role);
}
