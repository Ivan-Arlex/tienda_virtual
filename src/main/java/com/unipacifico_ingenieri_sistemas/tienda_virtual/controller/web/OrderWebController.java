package com.unipacifico_ingenieri_sistemas.tienda_virtual.controller.web;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.OrderDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.enums.OrderStatus;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.service.OrderService;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.service.PaymentService;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderWebController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserService userService;

    @GetMapping("/checkout")
    public String checkout(Authentication auth, Model model) {
        model.addAttribute("user", userService.findByUsername(auth.getName()));
        return "orders/checkout";
    }

    @PostMapping("/place")
    public String placeOrder(Authentication auth, RedirectAttributes attrs) {
        try {
            OrderDto order = orderService.createFromCart(auth.getName());
            return "redirect:/orders/" + order.getId() + "/confirm";
        } catch (Exception e) {
            attrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/{id}/confirm")
    public String confirmPage(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        return "orders/confirmation";
    }

    @PostMapping("/{id}/pay")
    public String processPayment(
            @PathVariable Long id,
            @RequestParam(defaultValue = "SIMULADO") String method,
            RedirectAttributes attrs) {
        try {
            paymentService.processPayment(id, method);
            attrs.addFlashAttribute("success", "¡Pago procesado exitosamente!");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/" + id + "/confirm";
    }

    @GetMapping("/history")
    public String history(Authentication auth, Model model) {
        model.addAttribute("orders", orderService.findByUsername(auth.getName()));
        return "orders/history";
    }
}
