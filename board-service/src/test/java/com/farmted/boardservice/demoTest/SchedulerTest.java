package com.farmted.boardservice.demoTest;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.util.Board1PageCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test") // Eureka Discovery Client 등록을 해제하기 위한 프로필
@DisplayName("1페이지 캐시 스케줄러 테스트 코드")
public class SchedulerTest {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private Board1PageCache board1PageCache;

    @BeforeEach
    void setUp() {
        // 레포지토리 초기화 후 캐시 클래스에 주입
        boardRepository.deleteAll();
        // 카테고리 별로 하나씩 생성
        for(BoardType category : BoardType.values()) {
            // PRODUCT의 경우, 판매+경매를 아우르는 카테고리기 때문에 생성은 하지 않음
            if (BoardType.PRODUCT.equals(category)) continue;
            boardRepository.save(Board.builder()
                    .boardType(category)
                    .boardTitle("Dummy Title")
                    .boardContent("Dummy Content")
                    .viewCount(0)
                    .boardStatus(true)
                    .memberName("Dummy Member")
                    .memberProfile("Dummy Profile")
                    .memberUuid("memberUuid")
                    .build());
        }
    }

    @Test
    @DisplayName("스케줄러 테스트")
    void cachingTest(){
        // given
        // when
        // 스케줄러에서 동작하는 JPA 메소드 그대로 실행
        Page<ResponseGetBoardDto> paging = boardRepository.findByBoardType(BoardType.PRODUCT,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
        // page1에 값이 생길 때까지 최대 2초 대기
        await().atMost(1500, MILLISECONDS).untilAsserted(
                () -> {
                    assertThat(board1PageCache.getPage1()).isNotNull();
                    assertThat(board1PageCache.getPage1().getTotalElements()).isGreaterThan(1);
                });
        // then
        assertThat(board1PageCache.getPage1().getContent().size()).isEqualTo(paging.getContent().size());
    }
}