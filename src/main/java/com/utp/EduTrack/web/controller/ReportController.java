package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.config.OpenApiConfig;
import com.utp.EduTrack.domain.dto.AcademicReportDTO;
import com.utp.EduTrack.domain.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
@Tag(name = "Reportes (ADMIN)", description = "RF15 - Generación de reportes institucionales agregados")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/academic")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener reporte académico institucional (ADMIN)", description = "Genera estadísticas de alumnos matriculados, inasistencias y listado de alumnos en riesgo")
    public ResponseEntity<AcademicReportDTO> getAcademicReport() {
        return ResponseEntity.ok(reportService.getAcademicReport());
    }
}
