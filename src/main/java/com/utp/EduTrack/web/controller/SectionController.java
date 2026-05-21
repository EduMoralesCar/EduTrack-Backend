package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.config.OpenApiConfig;
import com.utp.EduTrack.domain.dto.AssignTeacherDTO;
import com.utp.EduTrack.domain.dto.SectionCreateDTO;
import com.utp.EduTrack.domain.dto.SectionDTO;
import com.utp.EduTrack.domain.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
@Tag(name = "Secciones", description = "RF04 y RF05 - Lectura: todos. Gestión: solo ADMIN")
public class SectionController {

    private final SectionService sectionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @Operation(summary = "Listar secciones")
    public ResponseEntity<List<SectionDTO>> findAll() {
        return ResponseEntity.ok(sectionService.findAll());
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @Operation(summary = "Listar secciones por curso")
    public ResponseEntity<List<SectionDTO>> findByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(sectionService.findByCourse(courseId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    @Operation(summary = "Obtener sección por id")
    public ResponseEntity<SectionDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(sectionService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear sección (ADMIN)", description = "Campos: courseId, period, code. Docente se asigna en PUT /{id}/teacher")
    public ResponseEntity<SectionDTO> create(@Valid @RequestBody SectionCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar sección (ADMIN)")
    public ResponseEntity<SectionDTO> update(@PathVariable Long id, @Valid @RequestBody SectionCreateDTO request) {
        return ResponseEntity.ok(sectionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar sección (ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sectionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Asignar docente (ADMIN) (RF05)", description = "Campo: teacherId (usuario con rol TEACHER)")
    public ResponseEntity<SectionDTO> assignTeacher(
            @PathVariable Long id,
            @Valid @RequestBody AssignTeacherDTO request
    ) {
        return ResponseEntity.ok(sectionService.assignTeacher(id, request));
    }
}
