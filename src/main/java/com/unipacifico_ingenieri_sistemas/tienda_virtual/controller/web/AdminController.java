package com.unipacifico_ingenieri_sistemas.tienda_virtual.controller.web;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.CategoryDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.ProductDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.enums.OrderStatus;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final ReportService reportService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("report", reportService.getGeneralReport());
        model.addAttribute("recentOrders", orderService.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ─── Productos ────────────────────────────────────────────────────────────

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products/list";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/products/form";
    }

    @GetMapping("/products/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("productDto", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/products/form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute ProductDto dto, RedirectAttributes attrs) {
        try {
            productService.save(dto);
            attrs.addFlashAttribute("success", "Producto guardado correctamente");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/toggle")
    public String toggleProduct(@PathVariable Long id, RedirectAttributes attrs) {
        productService.toggleActive(id);
        attrs.addFlashAttribute("success", "Estado del producto actualizado");
        return "redirect:/admin/products";
    }

    // ─── Categorías ───────────────────────────────────────────────────────────

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("category", new CategoryDto());
        return "admin/categories/list";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute CategoryDto dto, RedirectAttributes attrs) {
        categoryService.save(dto);
        attrs.addFlashAttribute("success", "Categoría guardada correctamente");
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            categoryService.deleteById(id);
            attrs.addFlashAttribute("success", "Categoría eliminada");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", "No se puede eliminar: la categoría tiene productos asociados");
        }
        return "redirect:/admin/categories";
    }

    // ─── Órdenes ──────────────────────────────────────────────────────────────

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders/list";
    }

    @PostMapping("/orders/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            RedirectAttributes attrs) {
        orderService.updateStatus(id, status);
        attrs.addFlashAttribute("success", "Estado de la orden actualizado");
        return "redirect:/admin/orders";
    }
}
