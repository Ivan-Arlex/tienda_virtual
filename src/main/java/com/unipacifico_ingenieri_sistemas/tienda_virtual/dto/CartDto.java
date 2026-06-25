package com.unipacifico_ingenieri_sistemas.tienda_virtual.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartDto {
    private Long id;
    private List<CartItemDto> items;
    private BigDecimal total;
    private int itemCount;
}
