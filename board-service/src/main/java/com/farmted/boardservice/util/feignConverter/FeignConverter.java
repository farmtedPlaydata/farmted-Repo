package com.farmted.boardservice.util.feignConverter;

import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.FeignDomainType;
import com.farmted.boardservice.exception.FeignException;
import com.farmted.boardservice.util.GlobalResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class FeignConverter<T> {

    // Exception 타입을 기준으로 단일값 추출 혹은 성공 여부만 검사
    public T convertSingleVo(ResponseEntity<GlobalResponseDto<T>> entity,  FeignDomainType domainType, ExceptionType exceptionType) {
        GlobalResponseDto<T> responseDto = extractDto(entity, domainType);
        convertIsSuccess(responseDto, domainType, exceptionType);

        // Get 요청인 경우만 값 반환
        if (Objects.requireNonNull(exceptionType) == ExceptionType.GET) {
            // 값이 존재할 경우만
            if (responseDto.getData() == null) {
                throw new FeignException(domainType, ExceptionType.GET);
            }
            return responseDto.getData();
        }
        return null;
    }

    // ResponseEntity에서 GlobalDto 추출 및 null인 경우 예외처리
    private GlobalResponseDto<T> extractDto(ResponseEntity<GlobalResponseDto<T>> entity, FeignDomainType domainType) {
        return Optional.ofNullable(entity)
                .map(HttpEntity::getBody)
                .orElseThrow(() -> new FeignException(domainType));
    }

    // 성공 여부만 검사
    private void convertIsSuccess(GlobalResponseDto<T> responseDto, FeignDomainType domainType, ExceptionType exceptionType) {
        if (!responseDto.getIsSuccess()) {
            throw new FeignException(domainType, exceptionType);
        }
    }

    // 리스트 타입의 값을 반환할 때 사용
    public List<T> convertListVo(ResponseEntity<GlobalResponseDto<List<T>>> entity, FeignDomainType domainType, ExceptionType exceptionType) {
        // 리스트의 경우 0개가 반환될 수 있으니 일단 예외처리는 주석
//        if (responseList.isEmpty()) {
//            throw new FeignException(FeignDomainType.PRODUCT, ExceptionType.GETLIST);
//        }
        return Optional.of(entity)
                    .map(HttpEntity::getBody)
                    .orElseThrow(() -> new FeignException(domainType, exceptionType)).getData();
    }

}