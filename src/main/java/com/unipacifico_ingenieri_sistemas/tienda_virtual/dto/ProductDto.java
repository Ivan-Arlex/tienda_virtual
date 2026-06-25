package com.unipacifico_ingenieri_sistemas.tienda_virtual.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    // Para el formulario admin (select de categoría)
    private Long categoryId;
    // Para la visualización en templates
    private String categoryName;
    private boolean active = true;
}
