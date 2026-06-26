package com.unipacifico_ingenieria_sistemas.tienda_virtual.repository;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Cart;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUsers(Users users);
}
