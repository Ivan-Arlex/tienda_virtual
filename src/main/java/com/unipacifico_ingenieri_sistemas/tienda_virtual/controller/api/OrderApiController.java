package com.unipacifico_ingenieri_sistemas.tienda_virtual.controller.api;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.OrderDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.enums.OrderStatus;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    @GetMapping("/my")
    public ResponseEntity<List<OrderDto>> myOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.findByUsername(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(Authentication auth) {
        return ResponseEntity.ok(orderService.createFromCart(auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<OrderDto> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        OrderStatus status = OrderStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
