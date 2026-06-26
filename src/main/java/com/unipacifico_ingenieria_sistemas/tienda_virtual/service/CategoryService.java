package com.unipacifico_ingenieria_sistemas.tienda_virtual.service;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.CategoryDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.mapper.CategoryMapper;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Category;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> findAll() {
        return categoryRepository.findByOrderByNameAsc().stream()
            .map(categoryMapper::toDto)
            .collect(Collectors.toList());
    }

    public CategoryDto findById(Long id) {
        return categoryMapper.toDto(findEntityById(id));
    }

    public CategoryDto save(CategoryDto dto) {
        Category entity = categoryMapper.toEntity(dto);
        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    // package-private: usado por ProductService para asociar categoría
    Category findEntityById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + id));
    }
}
