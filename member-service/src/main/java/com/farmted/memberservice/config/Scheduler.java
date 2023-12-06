package com.farmted.memberservice.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Scheduled(cron = "0 0 0 * * *")    // 매일 자정
    public void oneDay() {
        
    }
}
