package com.farmted.auctionservice.repository;

import com.farmted.auctionservice.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction,Long>{

    // 낙찰자의 낙찰내역 조회
    public List<Auction> findAuctionByAuctionBuyer(String auctionBuyer);

    // 특정 판매자가 등록한 경매 내역
    public List<Auction> findAuctionByMemberUuid(String memberUuid);

    // 특정 경매 조회
    public Auction findAuctionByProductUuid(String productUuid);

    //특정 생성 시간에 대한 경매 조회
    public List<Auction> findAuctionByAuctionDeadline(LocalDate dateLine);

    // 상태값에 따른 경매 조회
    public List<Auction> findAuctionByAuctionStatus(boolean auctionStatus);

    // 게시글에 따른 경매 조회
    public Auction findAuctionByBoardUuid(String boardUuid);

}
