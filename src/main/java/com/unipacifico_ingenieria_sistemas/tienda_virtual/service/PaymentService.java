package com.unipacifico_ingenieria_sistemas.tienda_virtual.service;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.PaymentDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.mapper.PaymentMapper;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Order;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Payment;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.enums.OrderStatus;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final PaymentMapper paymentMapper;

    public PaymentDto processPayment(Long orderId, String method) {
        Order order = orderService.findEntityById(orderId);
        if (order.getStatus() != OrderStatus.PENDIENTE) {
            throw new IllegalStateException("La orden ya fue procesada anteriormente");
        }
        Payment payment = Payment.builder()
            .order(order)
            .amount(order.getTotal())
            .method(method)
            .status("APROBADO")
            .createdAt(LocalDateTime.now())
            .build();
        Payment saved = paymentRepository.save(payment);
        orderService.updateStatus(orderId, OrderStatus.PAGADO);
        return paymentMapper.toDto(saved);
    }
}
