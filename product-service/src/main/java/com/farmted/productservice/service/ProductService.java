package com.farmted.productservice.service;

import com.farmted.productservice.domain.Product;
import com.farmted.productservice.dto.request.ProductModifyRequestDto;
import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.response.ProductResponseDto;
import com.farmted.productservice.exception.ProductException;
import com.farmted.productservice.exception.SellerException;
import com.farmted.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 DB 등록
    public void saveProduct(String memberUuid,ProductSaveRequestDto productSaveRequestDto){
        Product saveProduct = productSaveRequestDto.toEntity(memberUuid);
        productRepository.save(saveProduct);
    }

    // 상품 DB  가격 수정
    public void modifyProduct(String productUuid, String memberUuId,ProductModifyRequestDto productModifyRequestDto){
        // 상품 판매자만 가격 수정 가능
        Product product = productRepository.findProductByUuidAndMemberUuid(productUuid,memberUuId)
                .orElseThrow(()-> new SellerException());
        if(!product.isAuctionStatus()){ // 경매 중이 아닌(상태값이 false) 경우만 가격 수정 가능
            product.modifyPrice(productModifyRequestDto.getMoney());
        }else{
            new ProductException(product.isAuctionStatus());
        }

    }


    // 판매자 등록한 전체 상품 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getListProductSeller(String memberUuid){
        // 해당 판매자가 존재하는 지 확인
        List<Product> productList= productRepository.findProductByMemberUuid(memberUuid)
               .orElseThrow(() -> new SellerException());

        List<ProductResponseDto> productListSeller = new ArrayList<>();
        for(Product product: productList){
            productListSeller.add(new ProductResponseDto(product));
        }
        return  productListSeller;
    }


    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ProductResponseDto getProductDetail(String productUuid){
      Product productDetail = productRepository.findProductByUuid(productUuid)
              .orElseThrow(()-> new RuntimeException("일단 해당 상품 없음 .. 차후 예외처리 신명나게 진행할 예정"));
      return new ProductResponseDto(productDetail);
    }

    // 전체 상품 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getListProduct() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> productAllList = new ArrayList<>();
        // TODO: 페이징 처리 진행 예정

        for (Product product : productList) {
            productAllList.add(new ProductResponseDto(product));
        }
        
        return productAllList;
    }
}
