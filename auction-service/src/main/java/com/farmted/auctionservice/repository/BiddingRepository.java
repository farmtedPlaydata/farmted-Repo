package com.farmted.auctionservice.repository;

import com.farmted.auctionservice.domain.Bidding;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface BiddingRepository extends JpaRepository<Bidding,Long> {
    public List<Bidding> findBiddingByMemberUuid(String memberUuid);

    // boardUuid와 MemberUuid로 입찰 횟수 확인
    public int countBiddingByBoardUuidAndMemberUuid(String boardUuid,String memberUuid);

    // boardUuid와 MemberUuid로 입찰가
    @Query("SELECT MAX(b.biddingPrice) FROM Bidding b WHERE b.boardUuid = :boardUuid AND b.memberUuid = :memberUuid")
    BigDecimal findMaxBiddingPriceByBoardUuidAndMemberUuid(@Param("boardUuid") String boardUuid, @Param("memberUuid") String memberUuid);

    //boardUuid와 MemberUuid로 입찰정보
    @Query("SELECT b FROM Bidding b WHERE b.boardUuid = :boardUuid AND b.memberUuid = :memberUuid ORDER BY b.biddingPrice DESC")
    Bidding findTopByBoardUuidAndMemberUuidOrderByBiddingPriceDesc(@Param("boardUuid") String boardUuid, @Param("memberUuid") String memberUuid);


    // 테스트 용
    public List<Bidding> findBiddingByBoardUuid(String boardUuid);
}
