package com.farmted.productservice.service;

import com.farmted.productservice.domain.Product;
import com.farmted.productservice.dto.request.ProductModifyRequestDto;
import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.response.ProductResponseDto;
import com.farmted.productservice.exception.ProductException;
import com.farmted.productservice.exception.SellerException;
import com.farmted.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

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
    public void modifyProduct(String boardUuid,ProductModifyRequestDto productModifyRequestDto,String memberUuid){
        // 상품 판매자만 가격 수정 가능
        Product product = productRepository.findProductByBoardUuidAndAuctionStatusTrue(boardUuid)
                .orElseThrow(()-> new ProductException());

        if(!product.getMemberUuid().equals(memberUuid))
           throw new SellerException();

        if(!product.isAuctionStatus()){ // 경매 중이 아닌(상태값이 false) 경우만 가격 수정 가능
            product.modifyPrice(productModifyRequestDto.getMoney());
        }else{
           throw new ProductException(product.isAuctionStatus());
        }

    }


    // 판매자 등록한 전체 상품 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getListProductSeller(String memberUuid,int pageNo){
        // 해당 판매자가 존재하는 지 확인
        Slice<Product> productList = productRepository.findProductByMemberUuid(memberUuid, PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createAt")));

        return productList.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());

    }


    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ProductResponseDto getProductDetail(String boardUuid){
      Product productDetail = productRepository.findProductByBoardUuid(boardUuid)
              .orElseThrow(()-> new ProductException());
      return new ProductResponseDto(productDetail);
    }

    // 전체 상품 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getListProduct(int pageNo) {
        Slice<Product> productList = productRepository.findAll(PageRequest.of(pageNo,3, Sort.by(Sort.Direction.DESC,"createAt")));


        return productList.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
}
