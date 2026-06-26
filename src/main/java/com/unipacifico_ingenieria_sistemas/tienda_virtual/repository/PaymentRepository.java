package com.unipacifico_ingenieria_sistemas.tienda_virtual.repository;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
}
