package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.RoleTypeException;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
    public void isAdmin(RoleEnums role){
        // 공지사항 작성 요청을 관리자가 하지 않으면 예외처리
        if(!RoleEnums.ADMIN.equals(role))
            throw new RoleTypeException(role, BoardType.NOTICE);
    }
}
