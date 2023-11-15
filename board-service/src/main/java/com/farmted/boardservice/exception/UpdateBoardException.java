package com.farmted.boardservice.exception;

// Update 로직을 수행하면서 발생하는 Custom Exception
public class UpdateBoardException extends RuntimeException{
    // 경매가 진행 중인 경매 게시글이라면 업데이트 불가능
    public UpdateBoardException() {
        super("경매가 진행 중인 상품이기에 반환 불가능");
    }
    // 이미 비활성화 상태거나 Uuid가 유효하지 않은 경우
    public UpdateBoardException(String boardUuid) {
        super("해당 게시글이 존재하지 않습니다.");
    }
}
