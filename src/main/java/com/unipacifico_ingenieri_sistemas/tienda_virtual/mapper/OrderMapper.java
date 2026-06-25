package com.unipacifico_ingenieri_sistemas.tienda_virtual.mapper;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.OrderDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.OrderItemDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.Order;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final PaymentMapper paymentMapper;

    public OrderItemDto toItemDto(OrderItem item) {
        if (item == null) return null;
        return OrderItemDto.builder()
            .id(item.getId())
            .productId(item.getProduct().getId())
            .productName(item.getProduct().getName())
            .quantity(item.getQuantity())
            .unitPrice(item.getUnitPrice())
            .subtotal(item.getSubtotal())
            .build();
    }

    public OrderDto toDto(Order order) {
        if (order == null) return null;
        List<OrderItemDto> items = order.getItems().stream()
            .map(this::toItemDto)
            .collect(Collectors.toList());
        return OrderDto.builder()
            .id(order.getId())
            .username(order.getUser().getUsername())
            .clientFullName(order.getUser().getFullName())
            .clientEmail(order.getUser().getEmail())
            .status(order.getStatus())
            .total(order.getTotal())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .items(items)
            .payment(paymentMapper.toDto(order.getPayment()))
            .build();
    }
}
