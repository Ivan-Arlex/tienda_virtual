package com.unipacifico_ingenieria_sistemas.tienda_virtual.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ReportDto {
    private Long totalOrders;
    private Long pendingOrders;
    private Long paidOrders;
    private Long shippedOrders;
    private Long deliveredOrders;
    private Long cancelledOrders;
    private BigDecimal totalRevenue;
    private Long totalProducts;
    private Long totalClients;
}
