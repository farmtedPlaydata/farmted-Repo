//package com.farmted.boardservice.util;
//
//import com.farmted.boardservice.enums.ExceptionType;
//import com.farmted.boardservice.enums.FeignDomainType;
//import com.farmted.boardservice.exception.FeignException;
//import com.farmted.boardservice.vo.AuctionVo;
//import com.farmted.boardservice.vo.ProductVo;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class CatchingFeign {
//    // Feign 통신으로 받은 값 역직렬화 해주는 클래스
//    private final ObjectMapper objectMapper;
//
//    // 어떤 도메인과 어떤 통신을 했는지 파라미터로 받아서
//        // 이쪽에서 Feign 전용 Exception 처리
//    public GlobalResponseDto<?> findFeignData(ResponseEntity<GlobalResponseDto<?>> responseEntity,
//                                           FeignDomainType domainType,
//                                           ExceptionType exceptionType) {
//        // 받아온 ResponseEntity의 Body가 null일 때 예외처리
//        GlobalResponseDto<?> body = Optional.ofNullable(responseEntity)
//                .map(HttpEntity::getBody)
//                .orElseThrow(()-> new FeignException(domainType));
//        try {
//            // 요청한 형식에 맞춰 ResponseDto 생성
//                //  개별 데이터인지, 전체 리스트인지, 성공 여부인지
//            switch(exceptionType){
//                case SAVE, UPDATE, DELETE, CHECK -> {return convertIsSuccess(body);}
//                case GET -> {return convertSingleDto(body,domainType);}
//                case GETLIST -> {return convertListDto(body, domainType);}
//                default -> {throw new FeignException(domainType);}
//            }
//            // 한번에 예외처리
//        } catch (JsonProcessingException | FeignException e) {
//            throw new FeignException(domainType, exceptionType);
//        }
//    }
//
//// ----------- 요청 컨버터 --------------
//    // 성공 여부만 확인하는 요청
//    private GlobalResponseDto<?> convertIsSuccess(GlobalResponseDto<?> globalDto) throws JsonProcessingException, FeignException{
//        boolean isSuccess = globalDto.getIsSuccess();
//        // 실패라면 예외 생성
//        if(!isSuccess) throw new FeignException();
//        return globalDto;
//    }
//
//    // 단일 값 요청
//    private GlobalResponseDto<?> convertSingleDto (GlobalResponseDto<?> globalDto, FeignDomainType domainType) throws JsonProcessingException{
//        return
//            switch(domainType){
//                case PRODUCT ->
//                    GlobalResponseDto.of(objectMapper.convertValue(globalDto, ProductVo.class));
//                case AUCTION ->
//                    GlobalResponseDto.of(objectMapper.convertValue(globalDto, AuctionVo.class));
//
//                default -> null;
//            };
//    }
//
//    // 리스트 값 요청
//    private GlobalResponseDto<?> convertListDto (GlobalResponseDto<?> globalDto, FeignDomainType domainType) throws JsonProcessingException{
//        switch (domainType){
//            case PRODUCT -> return globalDto.getData();
//        }
//        return GlobalResponseDto.of(dataList);
//    }
//}