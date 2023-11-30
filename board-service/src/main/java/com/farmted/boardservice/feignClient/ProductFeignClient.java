package com.farmted.boardservice.feignClient;

import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.util.GlobalResponseDto;
import com.farmted.boardservice.vo.ProductVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", path= "/product-service")
public interface ProductFeignClient {
    // 판매자 상품 게시글 등록
        // 반환 boolean
    @PostMapping("/products/boards")
    ResponseEntity<GlobalResponseDto<ProductVo>> createProductData(@RequestBody ProductVo productVo,
                                                        @RequestHeader("UUID") String uuid);

    // 판매자 등록 전체 상품 조회
        // 반환 List<ProductResponseDto>
    @GetMapping("/products/seller/{member_uuid}")
    ResponseEntity<GlobalResponseDto<List<ProductVo>>> getProductListSeller(
            @PathVariable (value = "member_uuid") String memberUuid,
            @RequestParam(value="category") BoardType category,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo);


    // 상품 정보 업데이트
        // 반환 boolean
    @PutMapping("/products/{board_uuid}/boards")
    ResponseEntity<GlobalResponseDto<ProductVo>> updateProductData(@PathVariable(value = "board_uuid") String boardUuid,
                                        @RequestBody ProductVo productVo,
                                        @RequestHeader("UUID") String uuid);

    // 전체 상품 조회
        // 반환 List<ProductResponseDto>
    @GetMapping("/products")
    ResponseEntity<GlobalResponseDto<List<ProductVo>>> getProductList(
            @RequestParam(value="category") BoardType category,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
            );

    // 상품 상세 조회
        // 단일 DTO
    @GetMapping("/products/{board_uuid}/boards")
    ResponseEntity<GlobalResponseDto<ProductVo>> getProductDetail(@PathVariable (value = "board_uuid") String boardUuid);


    // 게시글이 삭제되면 상품도 삭제 (경매는 어차피 종료된 상태임)
    @DeleteMapping("/products/{board_uuid}/boards")
    ResponseEntity<GlobalResponseDto<ProductVo>> deactiveProductStatus (@PathVariable(value = "board_uuid") String boardUuid,
                                   @RequestHeader("UUID") String uuid);
}
