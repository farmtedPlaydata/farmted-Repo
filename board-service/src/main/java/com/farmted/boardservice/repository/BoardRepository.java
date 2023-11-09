package com.farmted.boardservice.repository;

import com.farmted.boardservice.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByBoardUuID (String boardUuid);

    // 테스트용 sql
    Optional<Board> findByBoardTitle (String boardTitle);
}
