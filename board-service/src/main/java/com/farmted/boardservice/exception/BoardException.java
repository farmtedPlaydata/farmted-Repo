package com.farmted.boardservice.exception;

import com.farmted.boardservice.enums.ExceptionType;

// 게시글 자체에 관련한 익셉션처리용 클래스
public class BoardException extends RuntimeException{
    public BoardException(ExceptionType type) {

        super("게시글" + type.toString());
    }
}
