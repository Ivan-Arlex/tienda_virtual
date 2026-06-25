package com.unipacifico_ingenieri_sistemas.tienda_virtual.repository;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.Cart;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
