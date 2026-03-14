package com.neerad.store.controllers;


import com.neerad.store.dtos.ProductDto;
import com.neerad.store.entities.Product;
import com.neerad.store.mappers.ProductMapper;
import com.neerad.store.repositories.CategoryRepository;
import com.neerad.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

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
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder ucb
            // UriComponentsBuilder : it is needed to show HTTP status 201 as it requires Location header as part of the spec
    ) {
        var categoryId = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(categoryId == null){
            return ResponseEntity.badRequest().build();
        }
        var product = productMapper.toEntity(productDto);
        product.setCategory(categoryId);
        productRepository.save(product);
        productDto.setId(product.getId());

        var uri = ucb.path("/products/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(uri).body(productDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ){

        var categoryId = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(categoryId == null){
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }

         productMapper.updateEntity(productDto, product);
        product.setCategory(categoryId);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ){
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
         productRepository.delete(product);

        return ResponseEntity.noContent().build();
    }
}
