package com.unipacifico_ingenieria_sistemas.tienda_virtual.mapper;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.CartDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.CartItemDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Cart;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.CartItem;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartItemDto toItemDto(CartItem item) {
        if (item == null) return null;
        BigDecimal subtotal = item.getProduct().getPrice()
            .multiply(BigDecimal.valueOf(item.getQuantity()));
        return CartItemDto.builder()
            .id(item.getId())
            .productId(item.getProduct().getId())
            .productName(item.getProduct().getName())
            .productPrice(item.getProduct().getPrice())
            .productImageUrl(item.getProduct().getImageUrl())
            .categoryName(item.getProduct().getCategory() != null
                ? item.getProduct().getCategory().getName() : null)
            .quantity(item.getQuantity())
            .subtotal(subtotal)
            .availableStock(item.getProduct().getStock())
            .build();
    }

    public CartDto toDto(Cart cart) {
        if (cart == null) return null;
        List<CartItemDto> items = cart.getItems().stream()
            .map(this::toItemDto)
            .collect(Collectors.toList());
        BigDecimal total = items.stream()
            .map(CartItemDto::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        int itemCount = cart.getItems().stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
        return CartDto.builder()
            .id(cart.getId())
            .items(items)
            .total(total)
            .itemCount(itemCount)
            .build();
    }
}
