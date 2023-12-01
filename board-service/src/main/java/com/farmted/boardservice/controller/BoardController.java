package com.farmted.boardservice.controller;

import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.service.BoardService;
import com.farmted.boardservice.util.GlobalResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board API", description = "게시글에 대한 전반적인 요청을 수용하는 Board-Service API")
@RequestMapping("/board-service")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/boards")
    @Operation(summary = "게시글 작성", description = "카테고리별 게시글의 등록 요청")
    public ResponseEntity<GlobalResponseDto<?>> createBoard(
            @Valid @RequestBody RequestCreateBoardDto productBoardDto,
            @RequestHeader("UUID") String uuid,
            @RequestHeader("ROLE") String role){
        boardService.createBoard(productBoardDto, uuid, role);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

// 전체 조회
    @GetMapping(value = "/boards")
    @Operation(summary = "카테고리 게시글 리스트 조회", description = "N 페이지에 대한 경매 게시글 조회 요청")
    public ResponseEntity<GlobalResponseDto<?>> getBoardList(
                                            @RequestParam(value="category") BoardType category,
                                            @RequestParam(required = false,
                                                         defaultValue = "0",
                                                                value = "page") int pageNo){
        return ResponseEntity.ok(
                GlobalResponseDto.of(
                    boardService.getAuctionBoardList(category,pageNo-1)
        ));
    }

    @GetMapping(value = "/boards/{board_uuid}/auctions")
    @Operation(summary = "경매 게시글 상세 조회", description = "개별 경매 게시글에 대한 단일 조회 요청")
    public ResponseEntity<GlobalResponseDto<?>> getAuctionBoard(@PathVariable(value = "board_uuid") String boardUuid){
        return ResponseEntity.ok(
                GlobalResponseDto.of(
                    boardService.getAuctionBoard(boardUuid)
        ));
    }

// 작성자(판매자) 전용
    //
    @GetMapping(value = "/boards/seller/{seller_uuid}")
    @Operation(summary = "특정 이용자 게시글 전체 조회", description = "특정 사용자의 게시글 전체 조회")
    public ResponseEntity<GlobalResponseDto<?>> getBoardListWriter(
            @PathVariable (value = "seller_uuid") String uuid,
            @RequestParam(value="category") BoardType category,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ){
        return ResponseEntity.ok(
                GlobalResponseDto.of(
                        boardService.getWriterBoardList(category,pageNo-1, uuid)
        ));
    }


    @PutMapping(value = "/boards/{board_uuid}/auctions")
    @Operation(summary = "특정 게시글 수정", description = "개별 경매 게시글에 대한 수정 요청")
    public ResponseEntity<GlobalResponseDto<?>> updateAuctionBoard(@PathVariable(value= "board_uuid") String boardUuid,
                                                @Valid @RequestBody RequestUpdateProductBoardDto updateDTO,
                                                @RequestHeader("UUID") String uuid){
        boardService.updateAuctionBoard(updateDTO, boardUuid, uuid);
        return ResponseEntity.ok(
                GlobalResponseDto.of(true)
        );
    }

    @DeleteMapping(value = "/boards/{board_uuid}/auctions")
    @Operation(summary = "개별 경매 게시글 삭제", description = "개별 경매 게시글에 대한 삭제 요청")
    public ResponseEntity<GlobalResponseDto<?>> deleteAuctionBoard(@PathVariable(value = "board_uuid") String boardUuid,
                                                @RequestHeader("UUID") String uuid) {
        boardService.deleteAuctionBoard(boardUuid, uuid);
        return ResponseEntity.ok(
                GlobalResponseDto.of(true)
        );
    }
}
