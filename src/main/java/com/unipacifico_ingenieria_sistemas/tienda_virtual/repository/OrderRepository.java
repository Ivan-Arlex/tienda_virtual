package com.unipacifico_ingenieria_sistemas.tienda_virtual.repository;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Order;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Users;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUsersOrderByCreatedAtDesc(Users users);
    List<Order> findAllByOrderByCreatedAtDesc();

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status IN ('PAGADO', 'ENVIADO', 'ENTREGADO')")
    BigDecimal sumTotalByCompletedOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Long countByStatus(@Param("status") OrderStatus status);
}
