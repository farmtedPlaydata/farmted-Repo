package com.farmted.auctionservice.repository;

import com.farmted.auctionservice.domain.Bidding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BiddingRepository extends JpaRepository<Bidding,Long> {
    public List<Bidding> findBiddingByMemberUuid(String memberUuid);
}
