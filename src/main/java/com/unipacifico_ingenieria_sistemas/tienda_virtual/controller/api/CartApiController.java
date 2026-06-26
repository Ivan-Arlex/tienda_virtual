package com.unipacifico_ingenieria_sistemas.tienda_virtual.controller.api;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.CartDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart(Authentication auth) {
        return ResponseEntity.ok(cartService.getCart(auth.getName()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDto> addItem(
            @RequestBody Map<String, Object> body,
            Authentication auth) {
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());
        return ResponseEntity.ok(cartService.addItem(auth.getName(), productId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartDto> removeItem(
            @PathVariable Long itemId,
            Authentication auth) {
        return ResponseEntity.ok(cartService.removeItem(auth.getName(), itemId));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartDto> updateItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, Integer> body,
            Authentication auth) {
        return ResponseEntity.ok(cartService.updateQuantity(auth.getName(), itemId, body.get("quantity")));
    }
}
