package com.farmted.productservice.controller;

import com.farmted.productservice.dto.request.ProductModifyRequestDto;
import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.response.ProductResponseDto;
import com.farmted.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("product-service")
public class ProductController {

    private final ProductService productService;

    // 판매자 상품 등록
    @PostMapping("/products/boards")
    public ResponseEntity<?>  saveProduct(
            @RequestBody ProductSaveRequestDto productSaveRequestDto,
            @RequestHeader("UUID") String uuid // 멤버
    ) {
        productService.saveProduct(uuid,productSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 판매자 등록 전체 상품 조회
    @GetMapping("/products/seller/{member_uuid}")
    public ResponseEntity<List<ProductResponseDto>> getProductListSeller(@PathVariable (value = "member_uuid") String memberUuid) {
        return  ResponseEntity.ok(productService.getListProductSeller(memberUuid));
    }

    // 판매자 가격 수정
    @PatchMapping("/products/{board_uuid}/boards")
    public ResponseEntity<?> modifyProduct(
            @PathVariable (value = "board_uuid") String boardUuid ,
            @RequestHeader("UUID") String memberUuid, // 멤버
            @RequestBody ProductModifyRequestDto productModifyRequestDto
    )
    {
        productService.modifyProduct(boardUuid, productModifyRequestDto,memberUuid);
        return ResponseEntity.ok().build();
    }

    // 판매자 전체 수정


    // 전체 상품 조회
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getProductList(){
       return ResponseEntity.ok(productService.getListProduct());
    }

    // 상품 상세 조회
    @GetMapping("/products/{board_uuid}/boards")
    public ResponseEntity<ProductResponseDto> getProductDetail(@PathVariable (value = "board_uuid") String boardUuid){
        return ResponseEntity.ok(productService.getProductDetail(boardUuid));

    }

}


