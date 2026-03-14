package com.neerad.store.mappers;

import com.neerad.store.dtos.ProductDto;
import com.neerad.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
//@Mapper                    :- Generates UserMapperImpl at compile time
// componentModel = "spring" :- Adds @Component so Spring can inject it (Makes it a Spring Bean)

public interface ProductMapper {
    @Mapping(source = "category.id",target = "categoryId")
    ProductDto toDto(Product product);
    // toDto(user): Entity → DTO : Returning data to the client
    Product toEntity(ProductDto productDto);
    // toEntity(request) : Request → Entity : Creating a new record (POST)

    @Mapping(target = "id",ignore = true)
    void updateEntity(ProductDto productDto, @MappingTarget Product product);
    // it maps the data from productDto to Product Entity that's why it is annotated with @Mapping Target
}
