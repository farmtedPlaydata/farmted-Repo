package com.farmted.productservice.repository;

import com.farmted.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    // 특정 상품 조회
    public Optional<Product> findProductByUuid(String uuid);
    // 판매자가 등록한 특정 상품 조회
    public Optional<Product> findProductByUuidAndMemberUuid(String uuid,String memberUuid);
    // 판매자가 등록한 전체 상품 조회
    public Optional<List<Product>> findProductByMemberUuid(String uuid);



}

