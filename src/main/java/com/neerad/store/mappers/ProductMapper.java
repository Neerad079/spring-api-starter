package com.neerad.store.mappers;

import com.neerad.store.dtos.ProductDto;
import com.neerad.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
//@Mapper                    :- Generates UserMapperImpl at compile time
// componentModel = "spring" :- Adds @Component so Spring can inject it (Makes it a Spring Bean)

public interface ProductMapper {
    @Mapping(source = "category.id",target = "CategoryId")
    ProductDto toDto(Product product);
}
