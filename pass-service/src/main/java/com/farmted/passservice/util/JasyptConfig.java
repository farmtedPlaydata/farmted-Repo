package com.farmted.passservice.util;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JasyptConfig {
    @Value("${jasypt.encryptor.password}")
    private String JASTYPTKEY ;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(JASTYPTKEY);
        config.setAlgorithm("PBEWITHMD5ANDDES"); // 암호화 알고리즘
        config.setPoolSize("1");				 // 암호화 인스턴스 pool
        config.setKeyObtentionIterations("1000");   // 반복할 해싱 횟수
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
        config.setStringOutputType("base64");    // 인코딩 방식

        encryptor.setConfig(config);
        return encryptor;
    }
}