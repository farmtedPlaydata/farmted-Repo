package com.farmted.boardservice.feignClient;


import com.farmted.boardservice.vo.ProductVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service", path= "/product-service")
public interface ProductFeignClient {
    // boardUuid와 함께 상품 데이터 저장
        // 어차피 게시글을 통해서만 저장되니까 boards필요없나?
    @PostMapping("/products/boards")
    boolean createProductData(@RequestBody ProductVo productVo,
                              @RequestHeader("UUID") String uuid);
    // boardUuid를 통해 개별 상품 데이터 획득
    @GetMapping("/products/{board_uuid}/boards")
    ProductVo getProductData(@PathVariable(value = "board_uuid") String boardUuid);

    // created순으로 status가 true인 상품 페이지 번호에 맞는 Slice 조회
        // 페이징 자체의 정보는 Board가 가지면 되니까 상품/경매는 Slice만 받아와도 될듯
        // 호출할 때 미리 -1해서 요청할테니 그냥 페이징 처리 하면 됨
    @GetMapping("/products/{page}/boards")
    List<ProductVo> getProductListData(@PathVariable(value = "page") int page);


    // 게시글 업데이트
    @PatchMapping("/products/{board_uuid}/boards")
    boolean updateProductData(@PathVariable(value = "board_uuid") String boardUuid,
                              @RequestBody ProductVo productVo,
                            @RequestHeader("UUID") String uuid);

    // 게시글이 삭제되면 상품도 삭제 (경매는 어차피 종료된 상태임)
    @PutMapping("/products/{board_uuid}/boards")
    boolean deactiveProductStatus (@PathVariable(value = "board_uuid") String boardUuid,
                                   @RequestHeader("UUID") String uuid);
}
