package com.farmted.boardservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.FeignDomainType;
import com.farmted.boardservice.exception.FeignException;
import com.farmted.boardservice.vo.AuctionVo;
import com.farmted.boardservice.vo.ProductVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.google.common.reflect.TypeToken;
import com.fasterxml.jackson.databind.JavaType;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CatchingFeign {
    // Feign 통신으로 받은 값 역직렬화 해주는 클래스
    private final ObjectMapper objectMapper;

    // 어떤 도메인과 어떤 통신을 했는지 파라미터로 받아서
        // 이쪽에서 Feign 전용 Exception 처리
    public GlobalResponseDto<?> findFeignData(ResponseEntity<?> responseEntity,
                                           FeignDomainType domainType,
                                           ExceptionType exceptionType) {
        // 받아온 ResponseEntity의 Body가 null일 때 예외처리
        Object body = Optional.ofNullable(responseEntity)
                .map(ResponseEntity::getBody)
                .orElseThrow(()-> new FeignException(domainType));
        try {
            // 요청한 형식에 맞춰 ResponseDto 생성
                //  개별 데이터인지, 전체 리스트인지, 성공 여부인지
            switch(exceptionType){
                case SAVE, UPDATE, DELETE, CHECK -> {return convertIsSuccess(body);}
                case GET -> {return convertSingleDto(body,domainType);}
                case GETLIST -> {return convertListDto(body, domainType);}
                default -> {return GlobalResponseDto.of(null);}
            }
            // 한번에 예외처리
        } catch (JsonProcessingException | FeignException e) {
            throw new FeignException(domainType, exceptionType);
        }
    }

// ----------- 요청 컨버터 --------------
    // 성공 여부만 확인하는 요청
    private GlobalResponseDto<?> convertIsSuccess(Object body) throws JsonProcessingException, FeignException{
        boolean isSuccess = objectMapper.convertValue(body, Boolean.class);
        // 실패라면 예외 생성
        if(!isSuccess) throw new FeignException();
        return GlobalResponseDto.of(objectMapper.convertValue(body, Boolean.class));
    }

    // 단일 값 요청
    private GlobalResponseDto<?> convertSingleDto (Object body, FeignDomainType domainType) throws JsonProcessingException{
        return
            switch(domainType){
                case PRODUCT ->
                    GlobalResponseDto.of(objectMapper.convertValue(body, ProductVo.class));
                case AUCTION ->
                    GlobalResponseDto.of(objectMapper.convertValue(body, AuctionVo.class));

                default -> null;
            };
    }

    // 리스트 값 요청
    private GlobalResponseDto<?> convertListDto (Object body, FeignDomainType domainType) throws JsonProcessingException{
        TypeToken<?> typeToken;
        // Java의 switch 문은 컴파일 타임 상수(compile-time constant)를 요구
        // 그러나 new TypeToken<List<ProductVo>>과 같은 표현식은
        // 런타임에 동적으로 생성되는 코드이기 때문에 switch 문에서 사용불가
        if (domainType == FeignDomainType.PRODUCT) {
            typeToken = new TypeToken<List<ProductVo>>() {};
        } else if (domainType == FeignDomainType.AUCTION) {
            typeToken = new TypeToken<List<AuctionVo>>() {};
        } else {
            typeToken = new TypeToken<List<?>>() {};
        }
        JavaType javaType = objectMapper.getTypeFactory().constructType(typeToken.getType());
        List<?> dataList = objectMapper.convertValue(body, javaType);
        return GlobalResponseDto.of(dataList);
    }
}