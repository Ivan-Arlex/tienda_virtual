package com.unipacifico_ingenieria_sistemas.tienda_virtual.controller.web;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.ProductDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.CategoryService;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.OrderService;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.ProductService;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/vendedor")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
public class VendedorController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final ReportService reportService;

    @Value("${app.upload.dir:./uploads/products}")
    private String uploadDir;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("report", reportService.getGeneralReport());
        model.addAttribute("recentOrders", orderService.findAll().stream().limit(5).toList());
        return "vendedor/dashboard";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());
        return "vendedor/products/list";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("formAction", "/vendedor/products/save");
        model.addAttribute("cancelUrl", "/vendedor/products");
        return "vendedor/products/form";
    }

    @GetMapping("/products/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("productDto", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("formAction", "/vendedor/products/save");
        model.addAttribute("cancelUrl", "/vendedor/products");
        return "vendedor/products/form";
    }

    @PostMapping("/products/save")
    public String saveProduct(
            @ModelAttribute ProductDto dto,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes attrs) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                dto.setImageUrl(saveUploadedFile(imageFile));
            }
            productService.save(dto);
            attrs.addFlashAttribute("success", "Producto guardado correctamente");
        } catch (Exception e) {
            attrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vendedor/products";
    }

    @PostMapping("/products/{id}/toggle")
    public String toggleProduct(@PathVariable Long id, RedirectAttributes attrs) {
        productService.toggleActive(id);
        attrs.addFlashAttribute("success", "Estado del producto actualizado");
        return "redirect:/vendedor/products";
    }

    private String saveUploadedFile(MultipartFile file) throws IOException {
        Path dir = Paths.get(uploadDir).toAbsolutePath();
        Files.createDirectories(dir);
        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), dir.resolve(filename));
        return "/uploads/" + filename;
    }
}
