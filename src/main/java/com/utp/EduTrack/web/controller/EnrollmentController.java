package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.config.OpenApiConfig;
import com.utp.EduTrack.domain.dto.EnrollmentCreateDTO;
import com.utp.EduTrack.domain.dto.EnrollmentDTO;
import com.utp.EduTrack.domain.service.EnrollmentService;
import com.utp.EduTrack.domain.service.AuthService;
import com.utp.EduTrack.security.UserPrincipal;
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
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
@Tag(name = "Matrículas (ADMIN)", description = "RF04 - Consulta: ADMIN y TEACHER. Alta/baja: solo ADMIN")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final AuthService authService;

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    @Operation(summary = "Listar mis matrículas (STUDENT)", description = "Permite al estudiante ver las secciones donde está matriculado")
    public ResponseEntity<List<EnrollmentDTO>> findMyEnrollments() {
        UserPrincipal principal = authService.getCurrentPrincipal();
        return ResponseEntity.ok(enrollmentService.findByStudent(principal.getId()));
    }

    @GetMapping("/section/{sectionId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @Operation(summary = "Listar matrículas por sección", description = "ADMIN y TEACHER")
    public ResponseEntity<List<EnrollmentDTO>> findBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(enrollmentService.findBySection(sectionId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Matricular estudiante (ADMIN)", description = "Campos: studentId, sectionId")
    public ResponseEntity<EnrollmentDTO> enroll(@Valid @RequestBody EnrollmentCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enroll(request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @Operation(summary = "Actualizar estado de matrícula (ADMIN / TEACHER)", description = "Estados permitidos: ENROLLED, WITHDRAWN, AT_RISK")
    public ResponseEntity<EnrollmentDTO> updateStatus(
            @PathVariable Long id, 
            @RequestParam("status") com.utp.EduTrack.persistance.entity.EnrollmentStatus status) {
        return ResponseEntity.ok(enrollmentService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar matrícula (ADMIN)")
    public ResponseEntity<Void> unenroll(@PathVariable Long id) {
        enrollmentService.unenroll(id);
        return ResponseEntity.noContent().build();
    }
}
