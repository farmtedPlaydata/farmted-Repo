package com.farmted.passservice.service;

import com.farmted.passservice.dto.request.RequestCreatePassDto;
import com.farmted.passservice.dto.request.RequestLoginDto;
import com.farmted.passservice.dto.response.ResponseListDto;
import com.farmted.passservice.vo.MemberVo;

import java.util.List;

public interface PassService {

    void createPass(RequestCreatePassDto dto);
    String login(RequestLoginDto dto);
    void logout(String uuid);
    List<ResponseListDto> getPassByAll(int pageNo);
    String findUuidByEmail(String email);
    String reIssue(String uuid);
    void changeRoleByMemberService(String uuid);
    String setRole(RequestLoginDto dto);
    void updateRole(MemberVo memberVo);
}