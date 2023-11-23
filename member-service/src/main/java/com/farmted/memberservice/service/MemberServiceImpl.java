package com.farmted.memberservice.service;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
<<<<<<< Updated upstream
import com.farmted.memberservice.enums.RoleEnums;
=======
import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
>>>>>>> Stashed changes
import com.farmted.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void createMember(RequestCreateMemberDto dto) {
<<<<<<< Updated upstream
        dto.setMemberRole(RoleEnums.USER);
        Member member = dto.toEntity();
        memberRepository.save(member);
=======
        Member member = dto.toEntity();
        memberRepository.save(member);
    }

    @Override
    public void updateMember(RequestUpdateMemberDto dto, Member member) {
        Member upMember = memberRepository.getMemberByMemberUuid(member.getMemberUuid());
        upMember.updateMember(dto.getMemberName(), dto.getMemberAddress(), dto.getMemberAddressDetail(), dto.getMemberPhone());
        memberRepository.save(upMember);
        log.info("회원 정보 수정 완료");
>>>>>>> Stashed changes
    }

}
