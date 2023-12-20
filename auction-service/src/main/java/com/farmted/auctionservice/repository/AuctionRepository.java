package com.farmted.auctionservice.repository;

import com.farmted.auctionservice.domain.Auction;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction,Long>{

    // 낙찰자의 낙찰내역 조회
    public List<Auction> findAuctionByAuctionBuyer(String auctionBuyer);

    // 특정 판매자가 등록한 경매 내역
    public Slice<Auction> findAuctionByMemberUuid(String memberUuid, Pageable pageable);

    // 특정 경매 조회
    public Auction findAuctionByProductUuid(String productUuid);

    //특정 생성 시간에 대한 경매 조회
    public List<Auction> findAuctionByAuctionDeadline(LocalDateTime dateLine);

    // 상태값에 따른 경매 조회
    public List<Auction> findAuctionByAuctionStatus(boolean auctionStatus);

    // 게시글에 따른 경매 조회
    public Auction findAuctionByBoardUuid(String boardUuid);

    @Query("SELECT MAX(a.auctionPrice) FROM Auction a WHERE a.boardUuid = :boardUuid")
    BigDecimal findMaxAuctionPrice(@Param("board_uuid") String boardUuid);


}
