package com.farmted.passservice.controller;

import com.farmted.passservice.config.security.UserDetailsImpl;
import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.service.PassService;
import com.farmted.passservice.util.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pass-service")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;
    private final JwtProvider jwtProvider;

    @PostMapping("/passes")
    public ResponseEntity<?> createPass(@RequestBody RequestCreatePassDto dto) {
        passService.createPass(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginPass(@RequestBody RequestLoginDto dto, HttpServletResponse response) {
        String token = passService.login(dto);
        jwtProvider.setToken(token, TokenType.ACCESS, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response,
                                    HttpServletRequest request) {
        String uuid = userDetails.getPassword();
        jwtProvider.deleteCookie(response, request);
        passService.logout(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
