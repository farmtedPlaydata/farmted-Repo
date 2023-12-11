package com.farmted.auctionservice.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BiddingAdapterTest {
    private final String firstmemberUuid = "1";
    private final String secondmemberUuid = "2";
    private final String thirdmemberUuid = "3";
    private final String fourthmemberUuid = "4";
    private final String fifthmemberUuid = "5";

    private final Long auctionId = 123L;

    @Autowired private BiddingAdapter biddingAdapter;

    @Test
    public void TopBidding(){
        biddingAdapter.createBidding(auctionId,firstmemberUuid,100d);
        biddingAdapter.createBidding(auctionId, secondmemberUuid, 110d);
        biddingAdapter.createBidding(auctionId, thirdmemberUuid, 120d);
        biddingAdapter.createBidding(auctionId, fourthmemberUuid, 130d);
        biddingAdapter.createBidding(auctionId, fifthmemberUuid, 150d);

        biddingAdapter.createBidding(auctionId, secondmemberUuid, 160d);
        biddingAdapter.createBidding(auctionId, firstmemberUuid, 200d);

        List<Long> topBidding = biddingAdapter.getTopBidding(auctionId);

        Assertions.assertEquals(firstmemberUuid, topBidding.get(0));
    }


}