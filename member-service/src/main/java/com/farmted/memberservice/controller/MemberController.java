package com.farmted.memberservice.controller;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
import com.farmted.memberservice.dto.request.SearchMemberParam;
import com.farmted.memberservice.dto.response.MemberNameImageDto;
import com.farmted.memberservice.dto.response.ResponsePagingToListDto;
import com.farmted.memberservice.global.GlobalResponseDto;
import com.farmted.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member-service")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 상세 정보
    @PostMapping("/members/{email}")
    public ResponseEntity<?> createMember(@PathVariable String email,
                                          @RequestBody RequestCreateMemberDto dto) {
        memberService.createMember(dto, email);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    // 회원 정보 수정
    @PutMapping("/update/{uuid}")
    public ResponseEntity<?> updateMember(@PathVariable String uuid, @RequestBody RequestUpdateMemberDto dto) {
        memberService.updateMember(dto, uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    // 회원 탈퇴(본인)
    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<?> deleteMember(@PathVariable String uuid) {
        memberService.deleteMember(uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    // 회원 강퇴
    @DeleteMapping("/expel/{uuid}")
    public ResponseEntity<?> expelMember(@PathVariable String uuid) {
        memberService.deleteMember(uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    // 권한 변경
    @PutMapping("/master/{uuid}")
    public ResponseEntity<?> grantRole(@PathVariable String uuid,
                                       Member member) {
        memberService.grantRole(uuid, member.getMemberRole());
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    // 전체 회원 조회
    @GetMapping("/all-members")
    public ResponseEntity<?> getAllMember(SearchMemberParam param, Pageable pageable) {
//        return ResponseEntity.ok(memberService.getAllMember(param, pageable));
        ResponsePagingToListDto dto = memberService.getAllMember(param, pageable);
        return ResponseEntity.ok(GlobalResponseDto.of(dto));
    }

    @GetMapping("/member/info")
    public ResponseEntity<?> memberNameAndImage(@RequestHeader("UUID") String memberUuid) {
        return ResponseEntity.ok(GlobalResponseDto.of(memberService.memberNameAndImage(memberUuid)));
    }

    @PostMapping("/member/checkin")
    public ResponseEntity<?> memberCheckIn(@RequestHeader("UUID") String uuid) {
        memberService.checkIn(uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }
}
