package com.farmted.auctionservice.repository;

import com.farmted.auctionservice.domain.Bidding;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BiddingRepository extends JpaRepository<Bidding,Long> {
    public List<Bidding> findBiddingByMemberUuid(String memberUuid);

    // 테스트 용
    public List<Bidding> findBiddingByBoardUuid(String boardUuid);
}
