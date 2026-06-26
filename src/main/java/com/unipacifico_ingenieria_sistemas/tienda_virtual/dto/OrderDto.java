package com.unipacifico_ingenieria_sistemas.tienda_virtual.dto;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private Long id;
    private String username;
    private String clientFullName;
    private String clientEmail;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDto> items;
    private PaymentDto payment;
}
