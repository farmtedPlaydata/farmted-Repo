package com.farmted.memberservice.controller;

import com.farmted.memberservice.config.security.UserDetailsImpl;
import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
import com.farmted.memberservice.dto.request.SearchMemberDto;
import com.farmted.memberservice.dto.request.SearchMemberParam;
import com.farmted.memberservice.dto.response.MemberResponseDto;
import com.farmted.memberservice.repository.MemberRepository;
import com.farmted.memberservice.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member-service")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 회원 상세 정보
    @PostMapping("/members")
    public ResponseEntity<?> createMember(@RequestBody RequestCreateMemberDto dto) {
        memberService.createMember(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 회원 정보 수정
    @PutMapping("/update")
    public ResponseEntity<?> updateMember(@RequestBody RequestUpdateMemberDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.updateMember(dto, userDetails.getMember());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 회원 탈퇴(본인)
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMember(UserDetailsImpl userDetails) {
        String memberUuid = userDetails.getUsername();
        memberService.deleteMember(memberUuid);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 회원 강퇴
    @DeleteMapping("/expel/{uuid}")
    public ResponseEntity<?> expelMember(@PathVariable String uuid) {
        memberService.deleteMember(uuid);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 권한 변경
    @PutMapping("/master/{uuid}")
    public ResponseEntity<?> grantRole(@PathVariable String uuid,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.grantRole(uuid, userDetails.getMember().getMemberRole());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/all-members")
    public ResponseEntity<Page<MemberResponseDto>> getAllMember(SearchMemberParam param, Pageable pageable) {
        return ResponseEntity.ok(memberRepository.findAll(param, pageable));
    }
}
