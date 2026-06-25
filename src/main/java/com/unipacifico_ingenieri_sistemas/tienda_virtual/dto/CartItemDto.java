package com.unipacifico_ingenieri_sistemas.tienda_virtual.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productImageUrl;
    private String categoryName;
    private Integer quantity;
    private BigDecimal subtotal;
    private Integer availableStock;
}
