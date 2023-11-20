package com.farmted.passservice.service;

import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;

public interface PassService {

    void createPass(RequestCreatePassDto dto);

    String login(RequestLoginDto dto);

}
