package com.unipacifico_ingenieria_sistemas.tienda_virtual.controller.api;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.ReportDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportApiController {

    private final ReportService reportService;

    @GetMapping("/general")
    public ResponseEntity<ReportDto> generalReport() {
        return ResponseEntity.ok(reportService.getGeneralReport());
    }
}
