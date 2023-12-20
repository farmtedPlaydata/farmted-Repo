package com.farmted.auctionservice.service;

import com.farmted.auctionservice.domain.Auction;
import com.farmted.auctionservice.domain.Bidding;
import com.farmted.auctionservice.dto.ResponseBiddingDto.BiddingResponseDto;
import com.farmted.auctionservice.dto.requestBiddingDto.BiddingCreateRequestDto;
import com.farmted.auctionservice.repository.AuctionRepository;
import com.farmted.auctionservice.repository.BiddingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final RedissonClient redissonClient;
    private final String PREFIX = "Auction-Bidding::";


    @Transactional
    public void createBidding(BiddingCreateRequestDto biddingCreateRequestDto,String boardUuid,String memberUuid) {
        String lockName = PREFIX + boardUuid;

        // Redis 분산 락 획득
        RLock lock = redissonClient.getLock(lockName);
        boolean isLockAcquired = false;

        try {
            isLockAcquired = lock.tryLock(10,1, TimeUnit.SECONDS); //높은 값 사용 (예: 10초 이상): 락을 획득하기까지 대기하는 시간이 길지만 락 획득 경우가 많음
            Bidding savedBidding = biddingCreateRequestDto.toEntity(boardUuid, memberUuid);


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
            biddingRepository.save(savedBidding);
        }catch (InterruptedException e){
            log("락을 획득하지 못했습니다");
        }
        finally {
            // Redis 분산 락 해제
            lock.unlock();
        }

    }



    public List<BiddingResponseDto> getBiddingList(String memberUuid){
       List<Bidding> biddingByMemberUuid = biddingRepository.findBiddingByMemberUuid(memberUuid);
       return biddingByMemberUuid.stream()
               .map(BiddingResponseDto::new)
               .toList();
   }




}
