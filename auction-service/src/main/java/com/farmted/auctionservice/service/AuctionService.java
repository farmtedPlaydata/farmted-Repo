package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Auction;
import com.farmted.auctionservice.dto.requestDto.AuctionCreateRequestDto;
import com.farmted.auctionservice.dto.responseDto.AuctionBuyerResponseDto;
import com.farmted.auctionservice.dto.responseDto.AuctionSellerResponseDto;
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
        // 시강 처리 로직
        LocalDate auctionDeadline = auctionCreateRequestDto.getAuctionDeadline().plusDays(31);
        Auction createAuctionDto = auctionCreateRequestDto.toEntity(memberUuid,auctionDeadline);
        auctionRepository.save(createAuctionDto);

    }
    // 경매 종료
    @Scheduled(cron = "* 1 * * * *") // 매일 1시, 매분 진행
    public void finishAuction(){
        LocalDate current = LocalDate.now();
        List<Auction> auctionDeadline = auctionRepository.findAuctionByAuctionDeadline(current);
        for (Auction auction : auctionDeadline) {
            auction.setAuctionDeadlineForStatus();
        }
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
