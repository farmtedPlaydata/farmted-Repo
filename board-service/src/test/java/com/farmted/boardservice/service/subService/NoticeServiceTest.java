package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.RoleTypeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Notice-Service 테스트 코드")
class NoticeServiceTest {
    @Autowired
    private NoticeService noticeService;

    @Test
    @DisplayName("공지사항의 경우 관리자만 작성 가능하도록 Exception 처리")
    void isAdmin() {
        // given
        RoleEnums admin = RoleEnums.ADMIN;
        RoleEnums nonAdmin = RoleEnums.USER;
        // when
        noticeService.isAdmin(admin);
        // then
        Assertions.assertThrows(RoleTypeException.class, ()->noticeService.isAdmin(nonAdmin));
    }
}