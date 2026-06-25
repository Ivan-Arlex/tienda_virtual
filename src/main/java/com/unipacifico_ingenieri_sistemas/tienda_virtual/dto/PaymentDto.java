package com.unipacifico_ingenieri_sistemas.tienda_virtual.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentDto {
    private Long id;
    private BigDecimal amount;
    private String method;
    private String status;
    private LocalDateTime createdAt;
}
