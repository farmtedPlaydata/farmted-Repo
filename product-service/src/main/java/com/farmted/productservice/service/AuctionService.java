package com.farmted.productservice.service;

import com.farmted.productservice.FeignClient.ProductToAuctionFeignClient;
import com.farmted.productservice.domain.Product;
import com.farmted.productservice.dto.response.ProductAuctionResponseDto;
import com.farmted.productservice.dto.response.ProductResponseDto;
import com.farmted.productservice.enums.ProductType;
import com.farmted.productservice.exception.ProductException;
import com.farmted.productservice.repository.ProductRepository;
import com.farmted.productservice.vo.RequestAuctionCreateVo;
import com.farmted.productservice.vo.ResponseAuctionGetVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.farmted.productservice.enums.ProductType.PRODUCT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuctionService {

    private final ProductRepository productRepository;
    private final ProductToAuctionFeignClient productToAuctionFeignClient;

    // feign 통신: 경매 생성
    public void createProductToAuction(String productUuid){
        // 상품DB에서 가격과 생성시간을 가져옵니다.
        Product product = productRepository.findProductByUuid(productUuid)
                // 해당 상품이 있는지 확인
                .orElseThrow(ProductException::new);
        // 엔티티를 VO로 변환줍니다.
        RequestAuctionCreateVo auctionCreateVo = new RequestAuctionCreateVo(product);
        // 페인 통신 진행
        productToAuctionFeignClient.createProductToAuctionFeign(product.getMemberUuid(),auctionCreateVo);

    }

    // 전체 (상품 + 경매) 조회
    @Transactional(readOnly = true)
    public List<ProductAuctionResponseDto> getListProductAuction(int pageNo) {
        List<ProductAuctionResponseDto> productAuctionResponseDtoList= new ArrayList<>();

        Slice<Product> productList = productRepository.findProductByProductType(PRODUCT,PageRequest.of(pageNo,3, Sort.by(Sort.Direction.DESC,"createAt")));
        List<ResponseAuctionGetVo> auctionIng = productToAuctionFeignClient.getAuctionIng();

        for (Product product : productList) {
            // productList에서 현재 순회 중인 product의 productId와 동일한 ResponseAuctionGetVo 찾기
            Optional<ResponseAuctionGetVo> matchingAuction = auctionIng.stream()
                    .filter(auction -> auction.getProductUuid().equals(product.getUuid()))
                    .findFirst();

            // ResponseAuctionGetVo가 존재하면 합치고, 없으면 Product만 사용
            ProductAuctionResponseDto mergedDto = matchingAuction.map(auction -> new ProductAuctionResponseDto(product, auction))
                    .orElse(new ProductAuctionResponseDto(product));

            productAuctionResponseDtoList.add(mergedDto);
        }
        return productAuctionResponseDtoList;
    }



}
