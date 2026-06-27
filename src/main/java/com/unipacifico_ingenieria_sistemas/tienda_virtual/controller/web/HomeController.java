package com.unipacifico_ingenieria_sistemas.tienda_virtual.controller.web;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.UserDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.CategoryService;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.ProductService;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("featuredProducts", productService.findAvailable().stream().limit(8).toList());
        model.addAttribute("categories", categoryService.findAll());
        // 1. Llamamos al método que creamos con el SecurityContextHolder
        Optional<UserDto> usuarioOpt = userService.getUsuarioAutenticado();

  
        if (usuarioOpt.isPresent()) {
            UserDto userDto = usuarioOpt.get();
            model.addAttribute("usuarioLogueado", userDto);
            
            if ("ROLE_ADMIN".equals(userDto.getRoles())) {
                return "redirect:/admin/dashboard"; 
            }
            if ("ROLE_VENDEDOR".equals(userDto.getRoles())) {
                return "redirect:/vendedor/products/new"; 
            }
        

        return "home"; 
    }
    }

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            Model model) {

        if (search != null && !search.isBlank()) {
            model.addAttribute("products", productService.search(search));
            model.addAttribute("search", search);
        } else if (categoryId != null) {
            model.addAttribute("products", productService.findByCategory(categoryId));
            model.addAttribute("selectedCategory", categoryId);
        } else {
            model.addAttribute("products", productService.findAvailable());
        }
        model.addAttribute("categories", categoryService.findAll());
        return "products/catalog";
    }
}
