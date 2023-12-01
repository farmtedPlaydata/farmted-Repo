package com.farmted.auctionservice.controller.api;

import com.farmted.auctionservice.dto.requestDto.AuctionCreateRequestDto;
import com.farmted.auctionservice.dto.responseDto.AuctionStatusResponseDto;
import com.farmted.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("auction-service")
public class ApiController {

    private final AuctionService auctionService;

    // 경매 정보 생성 및 시작
    @PostMapping(value = "/product/auctions")
    public ResponseEntity<?> createAuction(
            @RequestHeader("UUID") String memberUuid,
            @RequestBody AuctionCreateRequestDto auctionCreateRequestDto){
        auctionService.createAuction(auctionCreateRequestDto,memberUuid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 방안 1
    // 경매 종료 상태 값 전달
//    @GetMapping(value = "/product/auctions")
//    public ResponseEntity<?> endAuction(){
//        System.out.println("스케쥴러 진행 중");
//        List<AuctionStatusResponseDto> auctionStatusResponseDtoList = auctionService.endAuctions();
//        return ResponseEntity.ok(auctionStatusResponseDtoList);
//    }

    //방안2
    @Scheduled(cron ="*/60 * * * * *") // (cron ="*/60 * * * * *") 매분 진행
    @GetMapping(value = "/product/auctions")
    public ResponseEntity<?> endAuction(){
        System.out.println("스케쥴러 진행 중");
        List<AuctionStatusResponseDto> auctionStatusResponseDtoList = auctionService.changeAuction();
        return ResponseEntity.ok(auctionStatusResponseDtoList);
    }
}
