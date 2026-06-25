package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.domain.dto.AttendanceDTO;
import com.utp.EduTrack.domain.dto.AttendanceJustificationDTO;
import com.utp.EduTrack.domain.service.AttendanceService;
import com.utp.EduTrack.persistance.entity.JustificationStatus;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.utp.EduTrack.config.OpenApiConfig;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
@Tag(name = "Asistencia (Attendance)", description = "RF09 y RF10 - Registro de asistencia y justificaciones")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "Registrar asistencia (RF09)", description = "Registra la asistencia de un estudiante para una sesión. Accesible por DOCENTE y ADMIN.")
    public ResponseEntity<AttendanceDTO> recordAttendance(@RequestBody AttendanceDTO dto) {
        return new ResponseEntity<>(attendanceService.recordAttendance(dto), HttpStatus.CREATED);
    }

    @PostMapping("/{attendanceId}/justify")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Solicitar justificación (RF10)", description = "Permite a un estudiante justificar una falta, con archivo de evidencia opcional. Accesible solo por ESTUDIANTE.")
    public ResponseEntity<AttendanceJustificationDTO> submitJustification(
            @PathVariable Long attendanceId,
            @RequestParam("reason") String reason,
            @RequestParam(value = "proofFile", required = false) MultipartFile proofFile) {
        return new ResponseEntity<>(attendanceService.submitJustification(attendanceId, reason, proofFile), HttpStatus.CREATED);
    }

    @PutMapping("/justifications/{justificationId}/resolve")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "Resolver justificación", description = "Permite a un docente o administrador aprobar o rechazar una justificación de inasistencia.")
    public ResponseEntity<Void> resolveJustification(
            @PathVariable Long justificationId,
            @RequestParam("status") JustificationStatus status) {
        attendanceService.resolveJustification(justificationId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/section/{sectionId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @Operation(summary = "Obtener registros de asistencia de una sección")
    public ResponseEntity<List<AttendanceDTO>> getSectionAttendance(@PathVariable Long sectionId) {
        return ResponseEntity.ok(attendanceService.getSectionAttendance(sectionId));
    }

    @GetMapping("/section/{sectionId}/justifications")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @Operation(summary = "Obtener solicitudes de justificación de una sección")
    public ResponseEntity<List<AttendanceJustificationDTO>> getSectionJustifications(@PathVariable Long sectionId) {
        return ResponseEntity.ok(attendanceService.getSectionJustifications(sectionId));
    }
}
