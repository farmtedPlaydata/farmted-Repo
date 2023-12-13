package com.farmted.boardservice.config;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.vo.MemberVo;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class InitDB {

    @Autowired
    private InitService initService;
    @PostConstruct
    public void init() {
        initService.createDummy();
    }
    public static final String MEMBER_UUID = "member-uuid";
    // 조회테스트 시 사용할 더미데이터 UUID 리스트
    public static List<String> BOARD_UUIDS;

    @Component
    @Transactional
    public static class InitService {
        private final EntityManager em;
        @Autowired
        public InitService(EntityManager em) {
            this.em = em;
        }

        public void createDummy(){
            BOARD_UUIDS = Arrays.stream(BoardType.values())
                    .filter(category -> !BoardType.PRODUCT.equals(category))
                    .map(category -> {
                        Board categoryBoard =
                            switch (category){
                                case PRODUCT -> null;
                                // 사진 추가해야하는 데이터만 추가
                                case AUCTION, SALE ->
                                    Board.builder()
                                            .boardType(category) // 무작위 값으로 변경
                                            .boardTitle("Random Board Title")
                                            .boardContent("Random Board Content")
                                            .viewCount(0) // 초기값
                                            .boardStatus(true) // 초기값
                                            .memberName("member-name")
                                            .memberProfile("profile")
                                            .memberUuid(MEMBER_UUID) // 무작위 UUID
                                            .boardUuid(UUID.randomUUID().toString()) // 무작위 UUID
                                            .productImage("ImageURL")
                                            .build();
                                case NOTICE, COMMISSION, CUSTOMER_SERVICE ->
                                        Board.builder()
                                                .boardType(category) // 무작위 값으로 변경
                                                .boardTitle("Random Board Title")
                                                .boardContent("Random Board Content")
                                                .viewCount(0) // 초기값
                                                .boardStatus(true) // 초기값
                                                .memberName("member-name")
                                                .memberProfile("profile")
                                                .memberUuid(MEMBER_UUID) // 무작위 UUID
                                                .boardUuid(UUID.randomUUID().toString()) // 무작위 UUID
                                                .build();
                            };
                        em.persist(categoryBoard);
                        return categoryBoard.getBoardUuid();
                    }).toList();
        }
    }
}