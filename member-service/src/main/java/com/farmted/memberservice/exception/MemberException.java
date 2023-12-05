package com.farmted.memberservice.exception;

public class MemberException extends RuntimeException {

    public MemberException(String msg) {
        super("문제가 발생했습니다. : " + msg);
    }
}
