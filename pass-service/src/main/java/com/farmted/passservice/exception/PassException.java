package com.farmted.passservice.exception;

public class PassException extends RuntimeException{

    public PassException(String msg) {
        super("문제가 발생했습니다 : " + msg);
    }
}
