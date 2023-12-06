package com.farmted.memberservice.service;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.dto.request.SearchMemberParam;
import com.farmted.memberservice.dto.response.MemberNameImageDto;
import com.farmted.memberservice.dto.response.MemberResponseDto;
import com.farmted.memberservice.dto.response.ResponsePagingToListDto;
import com.farmted.memberservice.enums.RoleEnums;
import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
import com.farmted.memberservice.exception.MemberException;
import com.farmted.memberservice.feignclient.PassFeignClient;
import com.farmted.memberservice.global.GlobalResponseDto;
import com.farmted.memberservice.repository.MemberRepository;
import com.farmted.memberservice.vo.PassVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PassFeignClient passFeignClient;

    @Transactional
    @Override
    public void createMember(RequestCreateMemberDto dto, String email) {
        try {
            ResponseEntity<?> re = passFeignClient.findByEmail(email);
            String uuid =((GlobalResponseDto<?>)re.getBody()).getData().toString();
            dto.setMemberRole(RoleEnums.USER);
            dto.setUuid(uuid);
            Member member = dto.toEntity();
            memberRepository.save(member);
        } catch (MemberException e) {
            throw new MemberException("MemberService - createMember");
        }
    }

    @Transactional
    @Override
    public void updateMember(RequestUpdateMemberDto dto, String uuid) {
        try {
            Member upMember = memberRepository.getMemberByMemberUuid(uuid);
            upMember.updateMember(dto);
            memberRepository.save(upMember);
        } catch (MemberException e) {
            throw new MemberException("MemberService - updateMember");
        }
    }

    @Transactional
    @Override
    public void deleteMember(String uuid) {
        Optional<Member> member = memberRepository.findByMemberUuid(uuid);
        if (member.isPresent()) {
            memberRepository.deleteMemberByMemberUuid(uuid);
        } else {
            throw new MemberException("MemberService - deleteMember");
        }
//        memberRepository.findByMemberUuid(uuid)
//                .ifPresentOrElse(
//                        member -> memberRepository.deleteMemberByMemberUuid(uuid),
//                        () -> new RuntimeException("ERROR : member-service - deleteMember"));
    }

    @Transactional
    @Override
    public void grantRole(String uuid, RoleEnums role) {
        Member member = memberRepository.findByMemberUuid(uuid)
                .orElseThrow(() -> new MemberException("MemberService - grantRole"));

        RoleEnums currentRole = member.getMemberRole();
        RoleEnums newRole = (currentRole == RoleEnums.USER)
                ? RoleEnums.ADMIN       // 현재 role이 USER이면 ADMIN으로 변경
                : RoleEnums.USER;       // ADMIN일 경우 USER로 변경
        member.changeRole(newRole);
    }

    @Override
    public ResponsePagingToListDto getAllMember(SearchMemberParam param, Pageable pageable) {
        Page<MemberResponseDto> pagingMember = memberRepository.findAll(param, pageable);
        ResponsePagingToListDto dto = new ResponsePagingToListDto();
        dto.setMemberList(pagingMember);
        return dto;
    }

    @Override
    public MemberNameImageDto memberNameAndImage(String uuid) {
        Member member = memberRepository.findByMemberUuid(uuid)
                .orElseThrow(() -> new MemberException("MemberService - memberNameAndImage"));
        return new MemberNameImageDto(member.getMemberName(), member.getMemberImage());
    }

    @Override
    @Transactional
    public void checkIn(String uuid) {
        Member member = memberRepository.findByMemberUuid(uuid)
                .orElseThrow(() -> new MemberException("MemberService - checkIn"));
        member.checkIn(member);
    }
}
