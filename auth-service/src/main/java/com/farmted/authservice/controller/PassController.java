package com.farmted.authservice.controller;

import com.farmted.authservice.dto.request.RequestCreatePassDto;
import com.farmted.authservice.dto.request.RequestLoginDto;
import com.farmted.authservice.service.PassService;
import com.farmted.authservice.service.PassServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pass-service")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;

    @PostMapping("/pass")
    public ResponseEntity<?> createPass(@RequestBody RequestCreatePassDto dto) {
        passService.createPass(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginPass(@RequestBody RequestLoginDto dto, HttpServletResponse response) {
        passService.login(dto, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
