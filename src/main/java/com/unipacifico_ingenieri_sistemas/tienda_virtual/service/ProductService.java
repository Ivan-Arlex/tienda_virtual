package com.unipacifico_ingenieri_sistemas.tienda_virtual.service;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.ProductDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.mapper.ProductMapper;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.Category;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.Product;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.repository.CategoryRepository;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<ProductDto> findAvailable() {
        return productRepository.findAvailableProducts().stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<ProductDto> findByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return productRepository.findByCategoryAndActiveTrue(category).stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<ProductDto> search(String query) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(query).stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }

    public ProductDto findById(Long id) {
        return productMapper.toDto(findEntityById(id));
    }

    public ProductDto save(ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (dto.getId() != null) {
            Product existing = findEntityById(dto.getId());
            productMapper.updateEntity(existing, dto, category);
            return productMapper.toDto(productRepository.save(existing));
        }
        Product newProduct = productMapper.toEntity(dto, category);
        return productMapper.toDto(productRepository.save(newProduct));
    }

    public void toggleActive(Long id) {
        Product product = findEntityById(id);
        product.setActive(!product.isActive());
        productRepository.save(product);
    }

    public void updateStock(Long id, int quantity) {
        Product product = findEntityById(id);
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Stock insuficiente para: " + product.getName());
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    public long count() {
        return productRepository.count();
    }

    // package-private: usado por CartService y OrderService
    Product findEntityById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }
}
