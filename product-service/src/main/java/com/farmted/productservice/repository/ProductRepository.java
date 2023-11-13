package com.farmted.productservice.repository;

import com.farmted.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    public Optional<Product> findProductByUuid(String uuid);
    public Optional<List<Product>> findProductByMemberUuid(String uuid);



}

