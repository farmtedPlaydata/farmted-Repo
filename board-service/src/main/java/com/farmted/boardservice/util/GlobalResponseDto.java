package com.farmted.boardservice.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // 필드가 null 값일 경우 그 필드 제외하고 보냄.
public class GlobalResponseDto<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess; //통신 결과를 보여주는 변수

    private final T data ; // 실제 DTO를 담는 변수

    @JsonCreator
    private GlobalResponseDto(@JsonProperty("data") T data){
        this.isSuccess = true;
        this.data =data;
    }


    private GlobalResponseDto(Boolean isSuccess){
        this.isSuccess = isSuccess;
        this.data = null;
    }


    public static GlobalResponseDto<Void> of(boolean isSuccess){
        return new GlobalResponseDto<>(isSuccess);
    }

    public static <T> GlobalResponseDto<T> of(T data){
        return new GlobalResponseDto<>(data);
    }
}
