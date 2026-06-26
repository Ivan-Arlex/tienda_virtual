package com.unipacifico_ingenieria_sistemas.tienda_virtual.mapper;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.CategoryDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        if (category == null) return null;
        return CategoryDto.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .productCount(category.getProducts() != null ? category.getProducts().size() : 0)
            .build();
    }

    public Category toEntity(CategoryDto dto) {
        return Category.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .build();
    }
}
