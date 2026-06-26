package com.unipacifico_ingenieria_sistemas.tienda_virtual.controller.web;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartWebController {

    private final CartService cartService;

    @GetMapping
    public String viewCart(Authentication auth, Model model) {
        model.addAttribute("cart", cartService.getCart(auth.getName()));
        return "cart/cart";
    }

    @PostMapping("/add")
    public String addItem(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication auth,
            RedirectAttributes attrs) {
        try {
            cartService.addItem(auth.getName(), productId, quantity);
            attrs.addFlashAttribute("success", "Producto agregado al carrito");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{itemId}")
    public String removeItem(@PathVariable Long itemId, Authentication auth) {
        cartService.removeItem(auth.getName(), itemId);
        return "redirect:/cart";
    }

    @PostMapping("/update/{itemId}")
    public String updateQuantity(
            @PathVariable Long itemId,
            @RequestParam int quantity,
            Authentication auth) {
        cartService.updateQuantity(auth.getName(), itemId, quantity);
        return "redirect:/cart";
    }
}
