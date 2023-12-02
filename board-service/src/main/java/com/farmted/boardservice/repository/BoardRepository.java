package com.farmted.boardservice.repository;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 게시글 조회의 경우 게시글 상태가 true인 경우만 받아와야함
    // 경매 게시글 전체 조회
        // 삭제되지 않은 해당 타입의 게시글 페이징
    Page<Board> findByBoardTypeAndBoardStatusTrue
            (BoardType boardType, Pageable pageable);
    // 판매자 등록 전체 상품 조회
    Page<Board> findByMemberUuidAndBoardTypeAndBoardStatusTrue
            (String memberUuid, BoardType boardType, Pageable pageable);
    // 게시글 상세 조회
    Optional<Board> findByBoardUuIDAndBoardStatusTrue(String boardUuid);
    Board getByBoardUuIDAndBoardStatusTrue(String boardUuid);
}
