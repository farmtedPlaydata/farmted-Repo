package com.farmted.memberservice.controller;

import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member-service")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<?> createMember(@RequestBody RequestCreateMemberDto dto) {
        memberService.createMember(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/sellers")
    public ResponseEntity<?> createSeller(@RequestBody RequestCreateMemberDto dto) {
        memberService.createSeller(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
