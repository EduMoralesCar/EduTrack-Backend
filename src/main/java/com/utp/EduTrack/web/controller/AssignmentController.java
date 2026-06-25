package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.domain.dto.AssignmentDTO;
import com.utp.EduTrack.domain.dto.AssignmentSubmissionDTO;
import com.utp.EduTrack.domain.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.utp.EduTrack.config.OpenApiConfig;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
@Tag(name = "Evaluaciones (Assignments)", description = "RF06 y RF07 - Gestión y entrega de evaluaciones")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "Crear nueva evaluación (RF06)", description = "Crea una nueva evaluación para una sección. Accesible por DOCENTE y ADMIN.")
    public ResponseEntity<AssignmentDTO> createAssignment(
            @ModelAttribute AssignmentDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return new ResponseEntity<>(assignmentService.createAssignment(dto, file), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "Actualizar evaluación", description = "Actualiza los detalles de una evaluación existente. Accesible por DOCENTE y ADMIN.")
    public ResponseEntity<AssignmentDTO> updateAssignment(
            @PathVariable Long id,
            @ModelAttribute AssignmentDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(assignmentService.updateAssignment(id, dto, file));
    }

    @GetMapping("/section/{sectionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    @Operation(summary = "Obtener evaluaciones por sección", description = "Lista todas las evaluaciones de una sección. Accesible por todos.")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsBySection(sectionId));
    }

    @PostMapping("/{assignmentId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Entregar evaluación (RF07)", description = "Permite a un estudiante subir un archivo local como entrega. Accesible solo por ESTUDIANTE.")
    public ResponseEntity<AssignmentSubmissionDTO> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("studentId") Long studentId,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(assignmentService.submitAssignment(assignmentId, studentId, comment, file), HttpStatus.CREATED);
    }

    @GetMapping("/section/{sectionId}/submissions")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    @Operation(summary = "Obtener entregas de evaluaciones por sección", description = "Lista todas las entregas realizadas para las evaluaciones de una sección. Accesible por DOCENTE, ADMIN y ESTUDIANTE.")
    public ResponseEntity<List<AssignmentSubmissionDTO>> getSectionSubmissions(@PathVariable Long sectionId) {
        return ResponseEntity.ok(assignmentService.getSectionSubmissions(sectionId));
    }
}
