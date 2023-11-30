package com.farmted.passservice.controller;

import com.farmted.passservice.config.security.UserDetailsImpl;
import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import com.farmted.passservice.enums.TokenType;
import com.farmted.passservice.service.PassService;
import com.farmted.passservice.util.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

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
        jwtProvider.setToken(token, TokenType.REFRESH, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/logout/{uuid}")
    public ResponseEntity<?> logout(@PathVariable String uuid,
                                    HttpServletResponse response,
                                    HttpServletRequest request) {
        passService.logout(uuid);
        jwtProvider.deleteCookie(response, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}