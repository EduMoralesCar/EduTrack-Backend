package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.domain.dto.FinalGradeDTO;
import com.utp.EduTrack.domain.dto.GradeDTO;
import com.utp.EduTrack.domain.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.utp.EduTrack.config.OpenApiConfig;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
@Tag(name = "Notas (Grades)", description = "RF08 - Registro y gestión de calificaciones")
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "Registrar nota (RF08)", description = "Registra una calificación para la entrega de un estudiante. La nota debe ser entre 1 y 20. Accesible por DOCENTE y ADMIN.")
    public ResponseEntity<GradeDTO> recordGrade(@RequestBody GradeDTO dto) {
        return new ResponseEntity<>(gradeService.recordGrade(dto), HttpStatus.CREATED);
    }

    @GetMapping("/section/{sectionId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "Obtener todas las notas individuales de una sección")
    public ResponseEntity<List<GradeDTO>> getSectionGrades(@PathVariable Long sectionId) {
        return ResponseEntity.ok(gradeService.getSectionGrades(sectionId));
    }

    @GetMapping("/section/{sectionId}/final")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "Obtener el consolidado de notas (PC1, PC2, PC3, PA, EXFINAL) de una sección")
    public ResponseEntity<List<FinalGradeDTO>> getSectionFinalGrades(@PathVariable Long sectionId) {
        return ResponseEntity.ok(gradeService.getSectionFinalGrades(sectionId));
    }
}
