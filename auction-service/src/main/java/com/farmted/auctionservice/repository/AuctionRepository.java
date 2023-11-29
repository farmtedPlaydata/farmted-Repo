package com.farmted.auctionservice.repository;

import com.farmted.auctionservice.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction,Long>{

    // 낙찰자의 낙찰내역 조회
    public List<Auction> findAuctionByAuctionBuyer(String auctionBuyer);

    // 특정 판매자가 등록한 경매 내역
    public List<Auction> findAuctionByMemberUuid(String memberUuid);

    // 특정 경매 조회
    public Auction findAuctionByBoardUuid(String boardUuid);
}
