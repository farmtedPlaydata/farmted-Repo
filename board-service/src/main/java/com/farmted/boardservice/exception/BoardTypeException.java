package com.farmted.boardservice.exception;

public class BoardTypeException extends RuntimeException{
        public BoardTypeException(String type) {
            throw new RuntimeException(type + " : 정해지지 않은 게시글 타입입니다.");
        }
}
