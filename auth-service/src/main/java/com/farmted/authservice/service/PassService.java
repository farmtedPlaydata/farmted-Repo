package com.farmted.authservice.service;

import com.farmted.authservice.dto.request.RequestCreatePassDto;

public interface PassService {

    void createPass(RequestCreatePassDto dto);
}
