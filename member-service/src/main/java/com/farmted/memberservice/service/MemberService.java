package com.farmted.memberservice.service;

import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
import com.farmted.memberservice.dto.request.SearchMemberParam;
import com.farmted.memberservice.dto.response.MemberNameImageDto;
import com.farmted.memberservice.dto.response.MemberResponseDto;
import com.farmted.memberservice.dto.response.ResponsePagingToListDto;
import com.farmted.memberservice.enums.RoleEnums;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    // 회원 상세 정보
    void createMember(RequestCreateMemberDto dto, String uuid);
    // 회원 정보 수정
    void updateMember(RequestUpdateMemberDto dto, String uuid);
    // 회원 삭제
    void deleteMember(String uuid);
    void grantRole(String uuid, RoleEnums role);
    ResponsePagingToListDto getAllMember(SearchMemberParam param, Pageable pageable);
    MemberNameImageDto memberNameAndImage(String uuid);
}
