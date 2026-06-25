package com.unipacifico_ingenieri_sistemas.tienda_virtual.controller.web;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.RegisterDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDto dto, RedirectAttributes attrs) {
        try {
            userService.register(dto);
            attrs.addFlashAttribute("success", "Registro exitoso. Inicia sesión con tus credenciales.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            attrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }
}
