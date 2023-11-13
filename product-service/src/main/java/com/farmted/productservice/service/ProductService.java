package com.farmted.productservice.service;

import com.farmted.productservice.domain.Product;
import com.farmted.productservice.dto.request.ProductModifyRequestDto;
import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.response.ProductResponseDTO;
import com.farmted.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 DB 등록
    public void saveProduct(ProductSaveRequestDto productSaveRequestDto){
        Product saveProduct = productSaveRequestDto.toEntity();
        productRepository.save(saveProduct);
    }

    // 상품 DB 수정
    public void modifyProduct(String productUuid,ProductModifyRequestDto productModifyRequestDto){
        Product product = productRepository.findProductByUuid(productUuid)
                .orElseThrow(()->new RuntimeException("일단 해당 상품 없음 .. 차후 예외처리 신명나게 진행할 예정"));
        if(!product.isAuctionStatus()){
            product.modifyPrice(productModifyRequestDto.getMoney());
        }else{
            new RuntimeException("경매 중에는 불가능합니다");
        }

    }

    @Transactional(readOnly = true)
    public List<Product> getListProductSeller(String memberUuid){
        List<Product> productListSeller= productRepository.findProductByMemberUuid(memberUuid)
               .orElseThrow(() -> new RuntimeException("일단 해당 판매자가 없음.. 차후 예외처리 할래"));
        return productListSeller;
    }


    @Transactional(readOnly = true)
    public ProductResponseDTO getProductDetail(String productUuid){
      Product productDetail = productRepository.findProductByUuid(productUuid)
              .orElseThrow(()-> new RuntimeException("일단 해당 상품 없음 .. 차후 예외처리 신명나게 진행할 예정"));
      return new ProductResponseDTO(productDetail);
    }

    @Transactional(readOnly = true)
    public List<Product> getListProduct() {
        List<Product> productList = productRepository.findAll(); // 경매 여부와 상관 없이 접부 출력, 판매 여부는 영향 있을까?
        return productList;
    }
}
