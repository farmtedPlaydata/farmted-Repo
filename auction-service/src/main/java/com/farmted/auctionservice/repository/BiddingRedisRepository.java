package com.farmted.auctionservice.repository;

import com.farmted.auctionservice.vo.RedisBidding;
import org.springframework.data.repository.CrudRepository;


public interface BiddingRedisRepository extends CrudRepository<RedisBidding,String> {
}
