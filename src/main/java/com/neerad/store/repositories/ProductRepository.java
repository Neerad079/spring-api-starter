package com.neerad.store.repositories;

import com.neerad.store.dtos.ProductDto;
import com.neerad.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
     List<Product> findByCategoryId(Byte categoryId);
}