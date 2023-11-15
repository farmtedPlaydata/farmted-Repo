package com.farmted.authservice.service;

import com.farmted.authservice.dto.request.RequestCreatePassDto;
import com.farmted.authservice.dto.request.RequestLoginDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface PassService {

    void createPass(RequestCreatePassDto dto);

    ResponseEntity<?> login(RequestLoginDto dto, HttpServletResponse response);

}
