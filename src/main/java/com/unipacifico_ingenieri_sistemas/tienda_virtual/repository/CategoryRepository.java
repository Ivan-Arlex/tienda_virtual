package com.unipacifico_ingenieri_sistemas.tienda_virtual.repository;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByOrderByNameAsc();
}
