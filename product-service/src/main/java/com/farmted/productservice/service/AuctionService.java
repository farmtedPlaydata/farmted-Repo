package com.farmted.productservice.service;

import com.farmted.productservice.FeignClient.ProductToAuctionFeignClient;
import com.farmted.productservice.domain.Product;
import com.farmted.productservice.dto.response.SaleAuctionTypeResponseDto;
import com.farmted.productservice.dto.response.SaleProductTypeResponseDto;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuctionService {

    private final ProductRepository productRepository;
    private final ProductToAuctionFeignClient productToAuctionFeignClient;

    // feign 통신: 경매 생성
    public void createProductToAuction(String productUuid) {
        // 상품DB에서 가격과 생성시간을 가져옵니다.
        Product product = productRepository.findProductByUuid(productUuid)
                // 해당 상품이 있는지 확인
                .orElseThrow(ProductException::new);
        // 엔티티를 VO로 변환줍니다.
        RequestAuctionCreateVo auctionCreateVo = new RequestAuctionCreateVo(product);
        // 페인 통신 진행
        productToAuctionFeignClient.createProductToAuctionFeign(product.getMemberUuid(), auctionCreateVo);

    }

    //feign 통신: 전체 (상품 + 경매) 목록 조회
    @Transactional(readOnly = true)
    public List<SaleProductTypeResponseDto> getListProductAuction(int pageNo) {
        Slice<Product> productList = productRepository.findAll(PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createAt")));
        return getProductsAuctionsResponseDto(productList);
    }

    //feign 통신: 판매자가 등록한 전체 (상품 + 경매) 목록 조회
    @Transactional(readOnly = true)
    public void getListMemberProductAuction(String memberUuid, int pageNo) {
        Slice<Product> productList = productRepository.findProductByMemberUuid(memberUuid, PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createAt")));
        getProductsAuctionsResponseDto(productList);
    }

    private List<SaleProductTypeResponseDto> getProductsAuctionsResponseDto(Slice<Product> productList) {
        List<ResponseAuctionGetVo> IngAuction = productToAuctionFeignClient.auctionProductList();

        return productList.stream()
                .map(product -> {
                    Optional<ResponseAuctionGetVo> matchingAuction = IngAuction.stream()
                            .filter(auction -> auction.productUuid().equals(product.getUuid()))
                            .findFirst();

                    SaleProductTypeResponseDto productAuctionResponseDto = new SaleProductTypeResponseDto(product);
                    matchingAuction.ifPresent(productAuctionResponseDto::mergeAuction);

                    return productAuctionResponseDto;
                })
                .collect(Collectors.toList());
    }

    //feign 통신: 경매 종료 상황 전달 받는 로직
    public void endAuctionFromProduct(String productUuid) {
        Product closedProduct = productRepository.findProductByUuid(productUuid)
                .orElseThrow(ProductException::new);
        closedProduct.closedStatus();
    }

    //feign 통신: 낙찰자 조회 시 상품 정보 포함
    public List<SaleAuctionTypeResponseDto> getMemberListAuction(String memberUuid, int pageNo) {
        Slice<Product> productList = productRepository.findProductByMemberUuid(memberUuid, PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createAt")));
        List<ResponseAuctionGetVo> ClosedAuction = productToAuctionFeignClient.auctionProductList();
        return productList.stream()
                .map(product -> {
                    Optional<ResponseAuctionGetVo> matchingAuction = ClosedAuction.stream()
                            .filter(auction -> auction.productUuid().equals(product.getUuid()))
                            .findFirst();

                    SaleAuctionTypeResponseDto AuctionResponseDto = new SaleAuctionTypeResponseDto(product);
                    matchingAuction.ifPresent(AuctionResponseDto::mergeAuction);

                    return AuctionResponseDto;
                })
                .collect(Collectors.toList());
    }
}