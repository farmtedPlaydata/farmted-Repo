package com.farmted.boardservice.enums;

// 발생하는 오류에 대한 기본 값
public enum ExceptionType {
    SAVE(" 저장에 실패했습니다."),
    GET(" 조회에 실패했습니다."),
    GETLIST(" 단체 조회에 실패했습니다."),
    UPDATE(" 갱신에 실패했습니다."),
    DELETE(" 삭제에 실패했습니다."),
    CHECK(" 확인에 실패했습니다."),
    UNKNOWN(" 통신 중 알 수 없는 오류가 발생했습니다.");

    private final String errorMessage;

    ExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
