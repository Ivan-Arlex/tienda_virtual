package com.unipacifico_ingenieria_sistemas.tienda_virtual.controller.web;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.CategoryService;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductWebController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "products/detail";
    }
}
