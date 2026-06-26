package com.unipacifico_ingenieria_sistemas.tienda_virtual.service;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.ReportDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.enums.OrderStatus;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;

    public ReportDto getGeneralReport() {
        long total = orderRepository.count();
        long pending  = safeCount(OrderStatus.PENDIENTE);
        long paid     = safeCount(OrderStatus.PAGADO);
        long shipped  = safeCount(OrderStatus.ENVIADO);
        long delivered = safeCount(OrderStatus.ENTREGADO);
        long cancelled = safeCount(OrderStatus.CANCELADO);

        BigDecimal revenue = orderRepository.sumTotalByCompletedOrders();
        if (revenue == null) revenue = BigDecimal.ZERO;

        return new ReportDto(total, pending, paid, shipped, delivered, cancelled,
            revenue, productService.count(), userService.countClients());
    }

    private long safeCount(OrderStatus status) {
        Long count = orderRepository.countByStatus(status);
        return count != null ? count : 0L;
    }
}
