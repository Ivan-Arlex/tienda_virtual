package com.unipacifico_ingenieri_sistemas.tienda_virtual.service;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.CartDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.mapper.CartMapper;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.*;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CartMapper cartMapper;

    // ── Métodos públicos (devuelven DTO, usados por controladores) ─────────────

    public CartDto getCart(String username) {
        User user = userService.findEntityByUsername(username);
        return cartMapper.toDto(getOrCreateCart(user));
    }

    public CartDto addItem(String username, Long productId, int quantity) {
        User user = userService.findEntityByUsername(username);
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (!product.isActive() || product.getStock() < quantity) {
            throw new IllegalArgumentException("Stock insuficiente o producto no disponible");
        }

        cartItemRepository.findByCartAndProduct(cart, product).ifPresentOrElse(
            item -> item.setQuantity(item.getQuantity() + quantity),
            () -> cart.getItems().add(
                CartItem.builder().cart(cart).product(product).quantity(quantity).build())
        );
        return cartMapper.toDto(cartRepository.save(cart));
    }

    public CartDto removeItem(String username, Long itemId) {
        User user = userService.findEntityByUsername(username);
        Cart cart = getOrCreateCart(user);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        return cartMapper.toDto(cartRepository.save(cart));
    }

    public CartDto updateQuantity(String username, Long itemId, int quantity) {
        if (quantity <= 0) return removeItem(username, itemId);
        User user = userService.findEntityByUsername(username);
        Cart cart = getOrCreateCart(user);
        cart.getItems().stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .ifPresent(item -> item.setQuantity(quantity));
        return cartMapper.toDto(cartRepository.save(cart));
    }

    // ── Métodos package-private (usados por OrderService) ─────────────────────

    Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() ->
            cartRepository.save(Cart.builder().user(user).build()));
    }

    BigDecimal calculateTotal(Cart cart) {
        return cart.getItems().stream()
            .map(item -> item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
