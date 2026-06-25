package com.unipacifico_ingenieri_sistemas.tienda_virtual.repository;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.Cart;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.CartItem;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
