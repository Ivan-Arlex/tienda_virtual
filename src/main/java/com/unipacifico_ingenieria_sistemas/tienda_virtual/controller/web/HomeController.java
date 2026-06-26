package com.unipacifico_ingenieria_sistemas.tienda_virtual.controller.web;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.CategoryService;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("featuredProducts", productService.findAvailable().stream().limit(8).toList());
        model.addAttribute("categories", categoryService.findAll());
        return "home/index";
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
