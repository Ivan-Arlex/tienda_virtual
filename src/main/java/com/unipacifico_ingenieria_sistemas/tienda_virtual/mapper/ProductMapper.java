package com.unipacifico_ingenieria_sistemas.tienda_virtual.mapper;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.ProductDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Category;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) return null;
        return ProductDto.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .imageUrl(product.getImageUrl())
            .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
            .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
            .active(product.isActive())
            .build();
    }

    public Product toEntity(ProductDto dto, Category category) {
        return Product.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .price(dto.getPrice())
            .stock(dto.getStock())
            .imageUrl(dto.getImageUrl())
            .category(category)
            .active(dto.isActive())
            .build();
    }

    public void updateEntity(Product product, ProductDto dto, Category category) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(category);
        product.setActive(dto.isActive());
    }
}
