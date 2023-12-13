package com.farmted.commentservice.util;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.dto.request.CommentCreateRequestDto;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.EnumMap;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;
    @PostConstruct
    public void init(){
        initService.initdb();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public void initdb(){
            String memberUUID = "memberUUID";
            String boardUUID = "boardUUID";
            em.persist(new CommentCreateRequestDto("댓글작성했습니다.1","이찬혁","comment1","user1","board1").toEntity(memberUUID, boardUUID));
            em.persist(new CommentCreateRequestDto("댓글작성했습니다.2","오강석","comment2","user2","board2").toEntity(memberUUID, boardUUID));
            em.persist(new CommentCreateRequestDto("댓글작성했습니다.3","노밤곧","comment3","user3","board3").toEntity(memberUUID, boardUUID));
            em.persist(new CommentCreateRequestDto("댓글작성했습니다.4","오밤걷","comment4","user4","board4").toEntity(memberUUID, boardUUID));
            em.flush();
            em.clear();
        }

    }

}
