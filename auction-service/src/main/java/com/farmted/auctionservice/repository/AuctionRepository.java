package com.farmted.auctionservice.repository;

import com.farmted.auctionservice.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction,Long>{

}
