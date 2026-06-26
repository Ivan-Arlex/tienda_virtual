package com.unipacifico_ingenieria_sistemas.tienda_virtual.service;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.CartDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.mapper.CartMapper;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Cart;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.CartItem;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Product;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Users;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.CartItemRepository;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.CartRepository;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.ProductRepository;
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
        Users users = userService.findEntityByUsername(username);
        return cartMapper.toDto(getOrCreateCart(users));
    }

    public CartDto addItem(String username, Long productId, int quantity) {
        Users users = userService.findEntityByUsername(username);
        Cart cart = getOrCreateCart(users);

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
        Users users = userService.findEntityByUsername(username);
        Cart cart = getOrCreateCart(users);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        return cartMapper.toDto(cartRepository.save(cart));
    }

    public CartDto updateQuantity(String username, Long itemId, int quantity) {
        if (quantity <= 0) return removeItem(username, itemId);
        Users users = userService.findEntityByUsername(username);
        Cart cart = getOrCreateCart(users);
        cart.getItems().stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .ifPresent(item -> item.setQuantity(quantity));
        return cartMapper.toDto(cartRepository.save(cart));
    }

    // ── Métodos package-private (usados por OrderService) ─────────────────────

    Cart getOrCreateCart(Users users) {
        return cartRepository.findByUsers(users).orElseGet(() ->
            cartRepository.save(Cart.builder().users(users).build()));
    }

    BigDecimal calculateTotal(Cart cart) {
        return cart.getItems().stream()
            .map(item -> item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    void clearCart(Users users) {
        Cart cart = getOrCreateCart(users);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
