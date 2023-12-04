package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Auction;
import com.farmted.auctionservice.dto.requestAuctionDto.AuctionCreateRequestDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionBuyerResponseDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionResponseDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionSellerResponseDto;
import com.farmted.auctionservice.dto.responseAuctionDto.AuctionStatusResponseDto;
import com.farmted.auctionservice.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;

    // 경매 정보 생성 및 시작
    public void createAuction(AuctionCreateRequestDto auctionCreateRequestDto, String memberUuid){
        // 시간 처리 로직
        LocalDate auctionDeadline = auctionCreateRequestDto.getAuctionDeadline().plusMonths(1);
        System.out.println(auctionDeadline+"########");
        Auction createAuctionDto = auctionCreateRequestDto.toEntity(memberUuid,auctionDeadline);
        auctionRepository.save(createAuctionDto);

    }

    // 방안1
    // 경매 종료 스케쥴링
    @Scheduled(cron ="*/60 * * * * *")
    public void changeAuction(){
        // 경매 종료 로직
        LocalDate current = LocalDate.now();
        List<Auction> auctionDeadline = auctionRepository.findAuctionByAuctionDeadline(current);
         System.out.println("경매 상태 확인 중");
        for (Auction auction : auctionDeadline) {
            auction.setAuctionDeadlineForStatus();
        }

    }

    public List<AuctionStatusResponseDto> endAuctions(){
        List<Auction> auctionStatusTrue = auctionRepository.findAuctionByAuctionStatus(true);
        return auctionStatusTrue.stream()
                .map(AuctionStatusResponseDto::new)
                .collect(Collectors.toList());
    }


    // 방안 2
//    @Scheduled(cron ="*/60 * * * * *")
//    public List<AuctionStatusResponseDto> changeAuction(){
//        // 경매 종료 로직
//        LocalDate current = LocalDate.now();
//        List<Auction> auctionDeadline = auctionRepository.findAuctionByAuctionDeadline(current);
//        System.out.println("경매 상태 확인 중");
//        for (Auction auction : auctionDeadline) {
//            auction.setAuctionDeadlineForStatus();
//        }
//        return auctionDeadline.stream()
//                .map(AuctionStatusResponseDto::new)
//                .collect(Collectors.toList());
//    }

    // 경매 목록 조회
    // TODO: 진행 종료 합쳐서 조회? 진행만 조회?
    public List<AuctionResponseDto> getAuctionList(){
//        List<Auction> auctionList = auctionRepository.findAll();
        List<Auction> auctionList = auctionRepository.findAuctionByAuctionStatus(false);
        return auctionList.stream()
                .map(AuctionResponseDto::new)
                .toList();
    }


    // 판매자 -> 낙찰 목록 조회 -> 경매 종료 상태
    public List<AuctionBuyerResponseDto> auctionBuyerList(String memberUuid){
        List<Auction> auctionByMemberList = auctionRepository.findAuctionByMemberUuid(memberUuid);
        return auctionByMemberList
                .stream()
                .map(AuctionBuyerResponseDto::new)
                .collect(Collectors.toList());
    }

    // 구매자 -> 낙찰 목록 조회 -> 경매 종료 상태
    public List<AuctionSellerResponseDto> auctionTrueList(String auctionBuyer){
        List<Auction> auctionSellerList = auctionRepository.findAuctionByAuctionBuyer(auctionBuyer);
        return auctionSellerList.stream()
                .map(AuctionSellerResponseDto::new)
                .collect(Collectors.toList());
    }


}
