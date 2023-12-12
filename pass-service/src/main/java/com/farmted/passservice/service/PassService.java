package com.farmted.passservice.service;

import com.farmted.passservice.domain.Pass;
import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import com.farmted.passservice.dto.response.ResponseListDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface PassService {

    void createPass(RequestCreatePassDto dto);
    String login(RequestLoginDto dto);
    void logout(String uuid);
    List<ResponseListDto> getPassByAll(int pageNo);
    String findUuidByEmail(String email);
    String reIssue(String uuid);
    void changeRoleByMemberService(String uuid);
}