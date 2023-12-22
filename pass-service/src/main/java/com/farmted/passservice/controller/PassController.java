package com.farmted.passservice.controller;

import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import com.farmted.passservice.dto.response.ResponseListDto;
import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.global.GlobalResponseDto;
import com.farmted.passservice.repository.PassRepository;
import com.farmted.passservice.service.PassService;
import com.farmted.passservice.util.cookies.CookieUtil;
import com.farmted.passservice.vo.MemberVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pass-service")
@RequiredArgsConstructor
@Tag(name = "Pass API", description = "인증 처리를 위한 Pass-Service API")
public class PassController {

    private final PassService passService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "health-check", description = "서버가 작동되고 있는지 체크")
    @GetMapping("/health-check")
    public String healthCheck() {
        return "server is available!!!";
    }

    @Operation(summary = "회원가입", description = "email, password를 입력받아 회원가입 처리, 기본 role = guest")
    @PostMapping("/passes")
    public ResponseEntity<?> createPass(@RequestBody RequestCreatePassDto dto) {
        passService.createPass(dto);
        String uuid = dto.getUuid();
        return ResponseEntity.ok(GlobalResponseDto.of(uuid));
    }

    @Operation(summary = "로그인", description = "로그인 시 쿠키에 토큰값, 권한 추가")
    @PostMapping("/login")
    public ResponseEntity<?> loginPass(@Valid @RequestBody RequestLoginDto dto, HttpServletResponse response) {
        String token = passService.login(dto);
        cookieUtil.setToken(token, TokenType.ACCESS, response);
        cookieUtil.setToken(token, TokenType.REFRESH, response);
        String role = passService.setRole(dto);
        cookieUtil.setRoleCookie(role, response);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 시 쿠키 삭제 및 redis에 저장된 RefreshToken 삭제")
    @GetMapping("/logout/{uuid}")
    public ResponseEntity<?> logout(@PathVariable String uuid,
                                    HttpServletResponse response,
                                    HttpServletRequest request) {
        passService.logout(uuid);
        cookieUtil.deleteCookie(response, request);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "전체 사용자 목록 조회")
    @GetMapping("/allpass")
    public ResponseEntity<?> findAll(@RequestParam(required = false, defaultValue = "0", value = "page")
                                     int pageNo) {
        List<ResponseListDto> allPass = passService.getPassByAll(pageNo);
        return ResponseEntity.ok(GlobalResponseDto.listOf(allPass));
    }

    @Operation(summary = "특정 사용자 조회", description = "Feign - Member-Service와의 통신용 email을 입력받아 uuid 검색")
    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        String uuid = passService.findUuidByEmail(email);
        return ResponseEntity.ok(GlobalResponseDto.of(uuid));
    }

    @Operation(summary = "엑세스 토큰 재발급", description = "사용자에게 RefreshToken이 있는 경우, 검증 후 엑세스 토큰을 재발급")
    @PostMapping("/reissue/{uuid}")
    public ResponseEntity<?> reIssue(@PathVariable String uuid,
                                     HttpServletResponse response) {
        String token = passService.reIssue(uuid);
        cookieUtil.setToken(token, TokenType.ACCESS, response);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "권한 수정", description = "Feign - Member-Service와 Pass-Service의 권한을 동일하게 만들어주기 위함")
    @PutMapping("/change-role/{uuid}")
    public ResponseEntity<?> changeRoleByMemberService(@PathVariable String uuid) {
        passService.changeRoleByMemberService(uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @Operation(summary = "회원 상세 정보 작성", description = "Feign - Member-Service에서 회원상세정보 작성 후 권한 재조정 및 토큰 재발급")
    @PostMapping("/update-role/{uuid}")
    public ResponseEntity<?> updateRole(@PathVariable String uuid,
                                        @RequestBody MemberVo memberVo) {
        passService.updateRole(uuid, memberVo);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }
}