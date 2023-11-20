package com.farmted.boardservice.exception;

// BoardType 에 대한 Custom Exception
public class BoardTypeException extends RuntimeException{
        public BoardTypeException(String type) {
            super(type + " : 정해지지 않은 게시글 타입입니다.");
        }
}
