package com.neerad.store.controllers;


import com.neerad.store.dtos.ProductDto;
import com.neerad.store.entities.Product;
import com.neerad.store.mappers.ProductMapper;
import com.neerad.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public Iterable<ProductDto> getAllProducts(
            @RequestParam(name = "categoryId", required = false) Byte categoryId
    ) {
//        if(!Set.of("categoryId","productId").contains(sortBy)){
//            sortBy = "categoryId";
//        }
        List<Product> products;
        if(categoryId !=null){
            products = productRepository.findByCategoryId(categoryId);
        }
        else{
            products = productRepository.findAll();
        }
        return products
                .stream()
                .map(productMapper::toDto)
                .toList();
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<ProductDto> getAllProductsByCategory(@PathVariable Long categoryId) {
        var product = productRepository.findById(categoryId).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }
}
