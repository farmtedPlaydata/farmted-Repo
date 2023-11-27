package com.farmted.memberservice.controller;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.enums.RoleEnums;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("local")
public class InitMember {

    private final InitMemberService initMemberService;

    @PostConstruct
    public void init() {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            for (int i=0; i<100; i++) {
                em.persist(new Member(UUID.randomUUID().toString(),"Test" + i,
                        RoleEnums.USER,
                        "010-0000-000" + i,
                        true));
            }
        }
    }
}
