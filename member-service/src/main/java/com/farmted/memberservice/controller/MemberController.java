package com.farmted.memberservice.controller;

import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
import com.farmted.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update")
    public ResponseEntity<?> updateMember(@RequestBody RequestUpdateMemberDto dto, @AuthenticationPrincipal UserDetails userDetails) {

    }

}
