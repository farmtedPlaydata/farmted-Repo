package com.farmted.memberservice.service;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.enums.RoleEnums;
import com.farmted.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void createMember(RequestCreateMemberDto dto) {
        dto.setMemberRole(RoleEnums.USER);
        Member member = dto.toEntity();
        memberRepository.save(member);
    }
}
