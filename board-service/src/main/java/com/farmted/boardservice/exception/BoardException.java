package com.farmted.boardservice.exception;

import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.ExceptionType;

// 게시글 자체에 관련한 익셉션처리용 클래스
public class BoardException extends RuntimeException{
    public BoardException(ExceptionType type) {
        super("게시글" + type.toString());
    }
    public BoardException(String type) {
        super(type + " : 정해지지 않은 게시글 타입입니다.");
    }
    public BoardException(BoardType boardType, ExceptionType exceptionType){ super(boardType.getTypeKo() + " : 해당 타입의 경우 "+ exceptionType.toString());}
}
