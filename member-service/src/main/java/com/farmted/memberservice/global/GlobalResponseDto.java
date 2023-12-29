package com.farmted.memberservice.global;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  // 필드가 null 값일 경우 해당 필드 제외하고 보냄
public class GlobalResponseDto<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;    // 통신 결과를 보여주는 변수

    private final T data;   // 실제 DTO를 담는 변수

    @JsonCreator
    public GlobalResponseDto(@JsonProperty("data") T data) {
        this.isSuccess = true;
        this.data = data;
    }

    private GlobalResponseDto(Boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.data = null;
    }

    // 로직 성공 여부만 판단할 때
    public static GlobalResponseDto<Void> of(boolean isSuccess) {
        return new GlobalResponseDto<>(isSuccess);
    }

    // 단일 Data를 반환해야 할 때
    public static <T> GlobalResponseDto<T> of(T data) {
        return new GlobalResponseDto<>(data);
    }

    // 다수의 Data를 반환해야 할 때
    public static <T> GlobalResponseDto<List<T>> listOf(List<T> data) {
        return new GlobalResponseDto<>(data);
    }
}
