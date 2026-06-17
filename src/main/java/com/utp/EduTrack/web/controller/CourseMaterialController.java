package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.domain.dto.CourseMaterialDTO;
import com.utp.EduTrack.domain.service.CourseMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Materiales del Curso", description = "Endpoints para subir y visualizar materiales de clase")
public class CourseMaterialController {

    private final CourseMaterialService materialService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "Subir material para una semana")
    public ResponseEntity<CourseMaterialDTO> uploadMaterial(
            @RequestParam Long sectionId,
            @RequestParam Integer weekNumber,
            @RequestParam String title,
            @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(materialService.uploadMaterial(sectionId, weekNumber, title, file), HttpStatus.CREATED);
    }

    @GetMapping("/section/{sectionId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "Obtener materiales de una sección")
    public ResponseEntity<List<CourseMaterialDTO>> getMaterialsBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(materialService.getMaterialsBySection(sectionId));
    }

    @PutMapping("/{materialId}/toggle-visibility")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "Cambiar estado de visibilidad del material (oculto/visto)")
    public ResponseEntity<CourseMaterialDTO> toggleVisibility(@PathVariable Long materialId) {
        return ResponseEntity.ok(materialService.toggleVisibility(materialId));
    }

    @DeleteMapping("/{materialId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "Eliminar un material didáctico por ID")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long materialId) {
        materialService.deleteMaterial(materialId);
        return ResponseEntity.noContent().build();
    }
}
