package com.unipacifico_ingenieri_sistemas.tienda_virtual.service;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.OrderDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.mapper.OrderMapper;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.*;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.enums.OrderStatus;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    public OrderDto createFromCart(String username) {
        User user = userService.findEntityByUsername(username);
        Cart cart = cartService.getOrCreateCart(user);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        BigDecimal total = cartService.calculateTotal(cart);
        Order order = Order.builder()
            .user(user)
            .status(OrderStatus.PENDIENTE)
            .total(total)
            .createdAt(LocalDateTime.now())
            .build();

        List<OrderItem> items = cart.getItems().stream().map(cartItem -> {
            productService.updateStock(cartItem.getProduct().getId(), cartItem.getQuantity());
            return OrderItem.builder()
                .order(order)
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getProduct().getPrice())
                .build();
        }).collect(Collectors.toList());

        order.setItems(items);
        Order saved = orderRepository.save(order);
        cartService.clearCart(user);
        return orderMapper.toDto(saved);
    }

    public OrderDto findById(Long id) {
        return orderMapper.toDto(findEntityById(id));
    }

    public List<OrderDto> findByUsername(String username) {
        User user = userService.findEntityByUsername(username);
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
            .map(orderMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<OrderDto> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(orderMapper::toDto)
            .collect(Collectors.toList());
    }

    public OrderDto updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = findEntityById(orderId);
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        return orderMapper.toDto(orderRepository.save(order));
    }

    public Long countByStatus(OrderStatus status) {
        Long count = orderRepository.countByStatus(status);
        return count != null ? count : 0L;
    }

    // package-private: usado por PaymentService
    Order findEntityById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + id));
    }
}
