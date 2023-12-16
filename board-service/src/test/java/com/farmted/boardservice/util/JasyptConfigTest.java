package com.farmted.boardservice.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JasyptConfigTest {
    String KEY = "key";
    @Test
    public void jasypt_암호화_복호화() {
        // given
        String normal1 = "암호화할 값1";
        String normal2 = "암호화할 값2";
        String normal3 = "암호화할 값3";
        String normal4 = "암호화할 값4";

        // when
        String enc1 = jasyptEncrypt(normal1);
        String enc2 = jasyptEncrypt(normal2);
        String enc3 = jasyptEncrypt(normal3);
        String enc4 = jasyptEncrypt(normal4);

        System.out.println("암호화 된 값 1: " + enc1);
        System.out.println("암호화 된 값 2: " + enc2);
        System.out.println("암호화 된 값 3: " + enc3);
        System.out.println("암호화 된 값 4: " + enc4);

        System.out.println("암호1 복호화: " + jasyptDecrypt(enc1));
        System.out.println("암호2 복호화: " + jasyptDecrypt(enc2));
        System.out.println("암호3 복호화: " + jasyptDecrypt(enc3));
        System.out.println("암호4 복호화: " + jasyptDecrypt(enc4));

        // then
        assertThat(normal1).isEqualTo(jasyptDecrypt(enc1));
        assertThat(normal2).isEqualTo(jasyptDecrypt(enc2));
        assertThat(normal3).isEqualTo(jasyptDecrypt(enc3));
        assertThat(normal4).isEqualTo(jasyptDecrypt(enc4));

    }

    private String jasyptEncrypt(String input) { // 암호화
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(KEY);
        return encryptor.encrypt(input);
    }

    private String jasyptDecrypt(String input){ // 복호화
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(KEY);
        return encryptor.decrypt(input);
    }
}