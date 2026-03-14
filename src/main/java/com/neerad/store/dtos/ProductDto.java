package com.neerad.store.dtos;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data

public class ProductDto {
    private Long Id;
    private String Name;
    private String Description;
    private Double Price;
    private Byte categoryId;
}
