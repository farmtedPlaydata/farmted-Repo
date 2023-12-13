package com.farmted.boardservice.repository;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetBoardDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
// 리스트 조회
    // 타입별 게시글 리스트 조회
    @Query("SELECT new com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto(b) " +
            "FROM Board b " +
            "WHERE ((b.boardType = :boardType AND b.boardType <> 'PRODUCT') OR " +
            "       (:boardType = 'PRODUCT' AND (b.boardType = 'AUCTION' OR b.boardType = 'SALE'))) " +
            "AND b.boardStatus = true")
    Page<ResponseGetBoardDto> findByBoardType(BoardType boardType, Pageable pageable);

    // 특정 회원의 타입별 게시글 리스트 조회
    @Query("SELECT new com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto(b) " +
            "FROM Board b " +
            "WHERE ((b.boardType = :boardType AND b.boardType <> 'PRODUCT') OR " +
            "       (:boardType = 'PRODUCT' AND (b.boardType = 'AUCTION' OR b.boardType = 'SALE'))) " +
            "AND b.boardStatus = true " +
            "AND b.memberUuid = :memberUuid")
    Page<ResponseGetBoardDto> findByMemberUuidAndBoardType(String memberUuid, BoardType boardType, Pageable pageable);

// 게시글 상세 조회
    @Query("SELECT new com.farmted.boardservice.dto.response.detailDomain.ResponseGetBoardDetailDto(b) " +
            "FROM Board b " +
            "WHERE b.boardUuid = :boardUuid AND b.boardStatus = true")
    Optional<ResponseGetBoardDetailDto> findDetailByBoardUuid(String boardUuid);

// 업데이트/삭제용 엔티티 불러오기 (영속성때문에)
    Optional<Board> findByBoardUuidAndBoardStatusTrue(String boardUuid);

    Board getByBoardUuidAndBoardStatusTrue(String boardUuid);
}
