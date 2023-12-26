package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Auction;
import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.dto.ResponseBiddingDto.BiddingResponseDto;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.feignClient.AuctionToMemberFeignClient;
import com.farmted.auctionservice.feignClient.AuctionToProductFeignClient;
import com.farmted.auctionservice.repository.AuctionRepository;
import com.farmted.auctionservice.repository.BiddingRepository;
import com.farmted.auctionservice.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.rmi.server.LogStream.log;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BiddingService {
    private final BiddingRepository biddingRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionToProductFeignClient auctionFeignClient;
    private final AuctionToMemberFeignClient memberFeignClient;
    private final RedissonClient redissonClient;
    private final String PREFIX = "Auction-Bidding::";


    @Transactional
    public void createBidding(BiddingCreateRequestDto biddingCreateRequestDto,String boardUuid,String memberUuid) {

        // 레디스 락 전에 입찰 내역 조회하여 첫 입찰 케이스, 추가 입찰 신청 케이스 구분 로직
        boolean isFirstBidder = biddingRepository.countBiddingByBoardUuidAndMemberUuid(boardUuid, memberUuid) == 0;

        if (!isFirstBidder) {
            // 첫 입찰 신청자가 아닌 경우 DB에서 값을 가져와서 추가 신청 금액을 합합니다.
            BigDecimal baseBidAmount= biddingRepository.findMaxBiddingPriceByBoardUuidAndMemberUuid(boardUuid, memberUuid);
            BigDecimal totalBidAmount = biddingCreateRequestDto.getBiddingPrice().add(baseBidAmount);
            biddingCreateRequestDto.setBiddingPrice(totalBidAmount);
        }
        //

        String lockName = PREFIX + boardUuid;

        // Redis 분산 락 획득
        RLock lock = redissonClient.getLock(lockName);
        boolean isLockAcquired = false;

        try {
            isLockAcquired = lock.tryLock(10,1, TimeUnit.SECONDS); //높은 값 사용 (예: 10초 이상): 락을 획득하기까지 대기하는 시간이 길지만 락 획득 경우가 많음
            Bidding savedBidding = biddingCreateRequestDto.toEntity(boardUuid, memberUuid);

            // 입찰 신청 내역은 무조건 저장
            biddingRepository.save(savedBidding);

            // 잔고 차감 Feign

            BigDecimal biddingPrice = savedBidding.getBiddingPrice();
            Auction auction = auctionRepository.findAuctionByBoardUuid(boardUuid);

            if (auction != null && auction.getAuctionBuyer() == null) {
                auction.setBiddingTop(biddingPrice, memberUuid);

            }else {
                BigDecimal maxAuctionPrice = auctionRepository.findMaxAuctionPrice(boardUuid);
                int comparisonResult = biddingPrice.compareTo(maxAuctionPrice);

                switch (comparisonResult) {
                    // 0 또는 음수: 현재 값이 데이터베이스의 최고값보다 작거나 같음으로 최고가가 갱신되지 않았습니다
                    case 0:
                    case -1:
                        log("현재 값이 데이터베이스의 최고값보다 작거나 같음으로 최고가가 갱신되지 않았습니다");
                        break;

                    // 양수: 현재 값이 데이터베이스의 최고값보다 큼으로 최고가를 저장하고 로그를 출력할 수 있습니다
                    case 1:
                        auction.setBiddingTop(biddingPrice, memberUuid);
                        break;
                    default:
                        // 비정상적인 상황 등에 대한 처리
                        log("비정상적인 상황이 발생했습니다");
                        break;
                }
            }
            System.out.println(biddingRepository.save(savedBidding).getBiddingPrice());
        }catch (InterruptedException e){
            log("락을 획득하지 못했습니다");
        }
        finally {
            // Redis 분산 락 해제
            lock.unlock();
        }

    }

    // 입찰 내역 조회
    public List<BiddingResponseDto> getBiddingList(String memberUuid) {
        List<Bidding> biddingList = biddingRepository.findBiddingByMemberUuid(memberUuid);
        List<BiddingResponseDto> createBiddingList = new ArrayList<>();
        for (Bidding bidding : biddingList) {
            ProductVo productDetail = auctionFeignClient.getProductDetail(bidding.getBoardUuid());
            BiddingResponseDto biddingDetailList = new BiddingResponseDto(bidding,productDetail);
            createBiddingList.add(biddingDetailList);
        }
        return createBiddingList;

    }

    // 입찰 복구 통신 로직
    public void setBalanceRecovery(String boardUuid){
        List<Bidding> biddingList = biddingRepository.findBiddingByBoardUuid(boardUuid);
        // memberUuid를 map에 넣어서 처리한 로직인지 확인
        for (Bidding bidding : biddingList) {
            BigDecimal price = biddingRepository.findMaxBiddingPriceByBoardUuidAndMemberUuid(boardUuid, bidding.getMemberUuid());
            memberFeignClient.failedBidBalance(bidding.getMemberUuid(), price.intValue());
        }

    }


}
