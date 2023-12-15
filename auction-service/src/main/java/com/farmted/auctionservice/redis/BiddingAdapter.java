//package com.farmted.auctionservice.redis;
//
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class BiddingAdapter {
//    //key 생성
//    private final String PREFIX = "Auction-BIDDING::";
//
//    //RedisTemplate
//    private final RedisTemplate<String, Long>  biddingRequestTemplate;
//
//    public BiddingAdapter(RedisTemplate<String, Long> biddingRequestTemplate) {
//        this.biddingRequestTemplate = biddingRequestTemplate;
//    }
//
//    // 경매에 참가하는  경매PK 를 사용하여 Sorted Set 의 key 를 생성
//    private String serializeKey(Long auctionId){
//        return PREFIX + auctionId;
//    }
//
//    // 입찰에 참여할 경매 아이디-> 상품pk, memberUuid, biddingPrice
//    public  Boolean createBidding(Long auctionId, String memberUuid, double biddingPrice){
//        String key = this.serializeKey(auctionId);
//        Long memberUuidAsLong = Long.valueOf(memberUuid);  // 문자열을 Long으로 변환
//        // ZSetOperations 의 add() 메서드를 사용->  key, memberUuid 에 biddingPrice를 score 로 저장
//        return biddingRequestTemplate.opsForZSet().add(key,memberUuidAsLong,biddingPrice);
//    }
//
//    // 입찰에 참여한 사용자들의 비딩 금액을 역순으로 정렬
//    public List<Long> getTopBidding(Long auctionId){
//        String key = this.serializeKey(auctionId);
//
//        //biddingPrice 금액이 높은 순으로 정렬해야 하기 때문에 score를 역순으로 조회하는 reverseRangeByScore()사용
//        // 이 때 socre 범위를 인자로 설정할 수 있는데
//        return biddingRequestTemplate
//                .opsForZSet()
//                .reverseRangeByScore(key,0,Double.MAX_VALUE,0,1)
//                .stream()
//                .collect(Collectors.toList());
//    }
//
//
//}
