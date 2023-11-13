package com.farmted.productservice.controller;

import com.farmted.productservice.domain.Product;
import com.farmted.productservice.dto.request.ProductModifyRequestDto;
import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.response.ProductResponseDTO;
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
    @PostMapping("/products")
    public ResponseEntity<?>  saveProduct(ProductSaveRequestDto productSaveRequestDto) {
        productService.saveProduct(productSaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 판매자 등록 전체 상품 조회
    @GetMapping("/products/{member_uuid}")
    public ResponseEntity<List<Product>> getProductListSeller(@PathVariable (value = "member_uuid") String memberUuid) {
        return  ResponseEntity.ok(productService.getListProductSeller(memberUuid));
    }

    // 판매자 가격 수정
    @PatchMapping("/products/{product_uuid}")
    public ResponseEntity<?> modifyProduct(@PathVariable (value = "product_uuid") String productUuid , ProductModifyRequestDto productModifyRequestDto)
    {
        productService.modifyProduct(productUuid,productModifyRequestDto);
        return ResponseEntity.ok().build();
    }

    // 판매자 가격 이외 수정 불가능? 가능


    // 전체 상품 조회
    @GetMapping("/products")
    public ResponseEntity<?> getProductList(){
       return ResponseEntity.ok(productService.getListProduct());
    }

    // 상품 상세 조회
    @GetMapping("/products/{product_uuid}")
    public ResponseEntity<ProductResponseDTO> getProductDetail(@PathVariable (value = "product_uuid") String productUuid){
        return ResponseEntity.ok(productService.getProductDetail(productUuid));

    }

}
