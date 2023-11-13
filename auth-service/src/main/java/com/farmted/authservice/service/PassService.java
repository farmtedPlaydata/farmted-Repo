package com.farmted.authservice.service;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.dto.request.RequestCreatePassDto;
import com.farmted.authservice.dto.request.RequestLoginDto;
import com.farmted.authservice.dto.response.ResponseLoginDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface PassService {

    void createPass(RequestCreatePassDto dto);

    ResponseEntity<ResponseLoginDto> login(RequestLoginDto dto);

}
