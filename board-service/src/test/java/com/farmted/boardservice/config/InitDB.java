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
                        Board categoryBoard = new RequestCreateBoardDto(
                                category,               // BoardType 값
                                "게시글 내용",                  // 게시글 내용
                                category.getTypeKo(),          // 게시글 제목
                                "상품 이름",                    // 상품 이름
                                10,                             // 상품 재고
                                10_000L,                         // 상품 가격
                                "상품 출처"                    // 상품 출처
                        ).toBoard(MEMBER_UUID, new MemberVo("member-name", "profile"));
                        em.persist(categoryBoard);
                        return categoryBoard.getBoardUuid();
                    }).toList();
        }
    }
}