package com.farmted.productservice.repository;

import com.farmted.productservice.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    // 특정 상품 조회
    public Optional<Product> findProductByBoardUuid(String boardUuid);

    // 판매자가 등록한 전체 상품 조회
    public Optional<List<Product>> findProductByMemberUuid(String memberUuid);

    // 판매자가 등록한 상품 중 경매 진행 중이 아닌 특정 상품 조회
    public Optional<Product> findProductByBoardUuidAndAuctionStatusTrue(String boardUuid);

    Slice<Product> findAllBy(Pageable pageable);

}

