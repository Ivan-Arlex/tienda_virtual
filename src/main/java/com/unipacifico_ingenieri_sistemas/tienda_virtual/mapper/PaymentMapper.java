package com.unipacifico_ingenieri_sistemas.tienda_virtual.mapper;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.PaymentDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDto toDto(Payment payment) {
        if (payment == null) return null;
        return PaymentDto.builder()
            .id(payment.getId())
            .amount(payment.getAmount())
            .method(payment.getMethod())
            .status(payment.getStatus())
            .createdAt(payment.getCreatedAt())
            .build();
    }
}
