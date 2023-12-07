package com.farmted.boardservice.controller;

import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;
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
            @RequestHeader("ROLE") RoleEnums role){
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
                    boardService.getBoardList(category,pageNo-1)
        ));
    }

    @GetMapping(value = "/boards/{board_uuid}/auctions")
    @Operation(summary = "경매 게시글 상세 조회", description = "개별 경매 게시글에 대한 단일 조회 요청")
    public ResponseEntity<GlobalResponseDto<?>> getAuctionBoard(@PathVariable(value = "board_uuid") String boardUuid){
        return ResponseEntity.ok(
                GlobalResponseDto.of(
                    boardService.getBoard(boardUuid)
        ));
    }

//    // TODO: 여기도 Product, SALE, AUCTION 카테고리 다 만들까?
//    @GetMapping(value = "/boards/buyer")
//    @Operation(summary = "본인 낙찰 리스트 전체 조회", description = "본인이 입찰한 경매 리스트를 조회합니다.")
//    public ResponseEntity<GlobalResponseDto<?>> getBoardListBuyer(
//            @RequestHeader ("UUID") String uuid,
//            @RequestParam(value="category") BoardType category,
//            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
//    ){
//        return ResponseEntity.ok(
//                GlobalResponseDto.of(
//                        boardService.getBuyerBoardList(category, pageNo-1, uuid)
//                ));
//    }

// 작성자(판매자) 전용
    @GetMapping(value = "/boards/seller/{seller_uuid}")
    @Operation(summary = "특정 이용자 작성 게시글 전체 조회", description = "특정 사용자가 작성한 게시글을 카테고리에 따라 리스트로 조회합니다.")
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
        boardService.updateBoard(updateDTO, boardUuid, uuid);
        return ResponseEntity.ok(
                GlobalResponseDto.of(true)
        );
    }

    @DeleteMapping(value = "/boards/{board_uuid}/auctions")
    @Operation(summary = "개별 경매 게시글 삭제", description = "개별 경매 게시글에 대한 삭제 요청")
    public ResponseEntity<GlobalResponseDto<?>> deleteAuctionBoard(@PathVariable(value = "board_uuid") String boardUuid,
                                                @RequestHeader("UUID") String uuid) {
        boardService.deleteBoard(boardUuid, uuid);
        return ResponseEntity.ok(
                GlobalResponseDto.of(true)
        );
    }
}
