package com.farmted.memberservice.config;

import com.farmted.memberservice.domain.Member;
import com.farmted.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *")    // 매일 자정
    private void checkInRefresh() {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            member.checkInRefresh();    // 출석체크 초기화
            memberRepository.save(member);
        }
    }
}
