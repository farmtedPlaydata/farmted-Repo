package com.farmted.passservice.controller;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import com.farmted.passservice.dto.response.ResponseListDto;
import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.global.GlobalResponseDto;
import com.farmted.passservice.repository.PassRepository;
import com.farmted.passservice.service.PassService;
import com.farmted.passservice.util.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pass-service")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;
    private final JwtProvider jwtProvider;

    @PostMapping("/passes")
    public ResponseEntity<?> createPass(@RequestBody RequestCreatePassDto dto) {
        passService.createPass(dto);
        String uuid = dto.getUuid();
        return ResponseEntity.ok(GlobalResponseDto.of(uuid));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginPass(@RequestBody RequestLoginDto dto, HttpServletResponse response) {
        String token = passService.login(dto);
        jwtProvider.setToken(token, TokenType.ACCESS, response);
        jwtProvider.setToken(token, TokenType.REFRESH, response);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @GetMapping("/logout/{uuid}")
    public ResponseEntity<?> logout(@PathVariable String uuid,
                                    HttpServletResponse response,
                                    HttpServletRequest request) {
        passService.logout(uuid);
        jwtProvider.deleteCookie(response, request);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @GetMapping("/allpass")
    public ResponseEntity<?> findAll(@RequestParam(required = false, defaultValue = "0", value = "page")
                                     int pageNo) {
        List<ResponseListDto> allPass = passService.getPassByAll(pageNo);
        return ResponseEntity.ok(GlobalResponseDto.listOf(allPass));
    }

    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        String uuid = passService.findUuidByEmail(email);
        return ResponseEntity.ok(GlobalResponseDto.of(uuid));
    }

    @PostMapping("/reissue/{uuid}")
    public ResponseEntity<?> reIssue(@PathVariable String uuid,
                                     HttpServletResponse response) {
        String token = passService.reIssue(uuid);
        jwtProvider.setToken(token, TokenType.ACCESS, response);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    @PutMapping("/change-role/{uuid}")
    public ResponseEntity<?> changeRoleByMemberService(@PathVariable String uuid) {
        passService.changeRoleByMemberService(uuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }
}