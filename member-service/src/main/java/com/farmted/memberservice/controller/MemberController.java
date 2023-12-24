package com.farmted.memberservice.controller;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.dto.request.RequestCreateMemberDto;
import com.farmted.memberservice.dto.request.RequestUpdateMemberDto;
import com.farmted.memberservice.dto.request.SearchMemberParam;
import com.farmted.memberservice.dto.response.MemberNameImageDto;
import com.farmted.memberservice.dto.response.ResponsePagingToListDto;
import com.farmted.memberservice.enums.RoleEnums;
import com.farmted.memberservice.feignclient.PassFeignClient;
import com.farmted.memberservice.global.GlobalResponseDto;
import com.farmted.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/member-service")
@RequiredArgsConstructor
@Tag(name= "Member API", description = "클라이언트에 대한 전반적인 요청을 처리하는 Member-service API")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "health-check", description = "서버가 작동되고 있는지 체크")
    @GetMapping("/health-check")
    public String healthCheck() {
       return "server is available!!!";
    }

    @Operation(summary = "멤버의 상세 정보 등록", description = "회원가입이 완료된 멤버의 상세정보를 작성, 완료 시 Role을 User로 변경")
    @PostMapping("/members")
    public ResponseEntity<?> createMember(@Valid @RequestPart("CREATE") RequestCreateMemberDto dto,
                                          @RequestPart("IMAGE")MultipartFile image) {
        memberService.createMember(dto, image);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "회원정보 수정", description = "이름, 주소, 전화번호 변경")
    @PutMapping("/update/{uuid}")
    public ResponseEntity<?> updateMember(@PathVariable String uuid, @RequestBody RequestUpdateMemberDto dto) {
        memberService.updateMember(dto, uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMember(@RequestHeader("UUID") String uuid) {
        memberService.deleteMember(uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "회원 강퇴", description = "Role이 Admin인 사용자가 다른 사용자를 강퇴할 때 사용")
    @DeleteMapping("/expel/{uuid}")
    public ResponseEntity<?> expelMember(@RequestHeader("ROLE") String role,
                                         @PathVariable String uuid) {
//        memberService.deleteMember(uuid);
        memberService.expelMember(role, uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }


    @Operation(summary = "권한 변경", description = "Role이 Master인 사용자가 다른 사용자의 권한을 변경할 때 사용. User <-> Admin")
    @PutMapping("/master/{uuid}")
    public ResponseEntity<?> grantRole(@PathVariable String uuid,
                                       Member member) {
        memberService.grantRole(uuid, member.getMemberRole());
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "전체 사용자 목록 조회")
    @GetMapping("/all-members")
    public ResponseEntity<?> getAllMember(SearchMemberParam param, Pageable pageable) {
//        return ResponseEntity.ok(memberService.getAllMember(param, pageable));
        ResponsePagingToListDto dto = memberService.getAllMember(param, pageable);
        return ResponseEntity.ok(GlobalResponseDto.of(dto));
    }

    @Operation(summary = "사용자의 이름과 프로필이미지 전달", description = "Feign - Board-Service와 Comment-Service")
    @GetMapping("/member/info")
    public ResponseEntity<?> memberNameAndImage(@RequestHeader("UUID") String memberUuid) {
        return ResponseEntity.ok(GlobalResponseDto.of(memberService.memberNameAndImage(memberUuid)));
    }

    @Operation(summary = "출석체크", description = "하루 한 번만 가능, 매일 자정 초기화, 출석체크 시 포인트 +1000")
    @PostMapping("/member/checkin")
    public ResponseEntity<?> memberCheckIn(@RequestHeader("UUID") String uuid) {
        memberService.checkIn(uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "권한 전달", description = "Feign - Member-Service와 Pass-Service의 권한을 동일하게 만들어주기 위함")
    @PostMapping("/member/update-role/{uuid}")
    public ResponseEntity<?> updateRole(@PathVariable String uuid) {
        return ResponseEntity.ok(GlobalResponseDto.of(memberService.updateRole(uuid)));
    }

    @Operation(summary = "입찰", description = "입찰 시 잔고 변화")
    @PutMapping("/after-bid/{uuid}")
    public ResponseEntity<?> afterBidBalance(@PathVariable String uuid, @RequestBody int balance) {
        memberService.afterBid(uuid, balance);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "낙찰 실패", description = "낙찰 실패 시 잔고 변화")
    @PutMapping("/after-bid/{uuid}")
    public ResponseEntity<?> failedBidBalance(@PathVariable String uuid, int balance) {
        memberService.failedBid(uuid, balance);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }
}
