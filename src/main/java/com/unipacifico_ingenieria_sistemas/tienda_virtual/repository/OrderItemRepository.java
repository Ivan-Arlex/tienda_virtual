package com.unipacifico_ingenieria_sistemas.tienda_virtual.repository;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
