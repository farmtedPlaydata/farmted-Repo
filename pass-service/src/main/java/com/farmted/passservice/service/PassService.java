package com.farmted.passservice.service;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface PassService {

    void createPass(RequestCreatePassDto dto);
    String login(RequestLoginDto dto);
    void logout(String uuid);
    Iterable<Pass> getPassByAll();
}