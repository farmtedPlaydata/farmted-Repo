package com.farmted.authservice.service;

import com.farmted.authservice.domain.Pass;
import com.farmted.authservice.dto.request.RequestCreatePassDto;

import java.util.List;

public interface PassService {

    void createPass(RequestCreatePassDto dto);


}
