package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.config.OpenApiConfig;
import com.utp.EduTrack.domain.dto.NotificationDTO;
import com.utp.EduTrack.domain.service.AuthService;
import com.utp.EduTrack.domain.service.NotificationService;
import com.utp.EduTrack.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
@Tag(name = "Notificaciones (ESTUDIANTE)", description = "RF16 - Alertas académicas automáticas para el estudiante")
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthService authService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Obtener notificaciones del alumno", description = "Lista las notificaciones del alumno logueado ordenadas por fecha")
    public ResponseEntity<List<NotificationDTO>> getMyNotifications() {
        UserPrincipal principal = authService.getCurrentPrincipal();
        return ResponseEntity.ok(notificationService.getNotificationsForStudent(principal.getId()));
    }

    @GetMapping("/unread-count")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Obtener cantidad de notificaciones no leídas")
    public ResponseEntity<Map<String, Long>> getMyUnreadCount() {
        UserPrincipal principal = authService.getCurrentPrincipal();
        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", notificationService.getUnreadCount(principal.getId()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Marcar notificación como leída")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Marcar todas las notificaciones como leídas")
    public ResponseEntity<Void> markAllAsRead() {
        UserPrincipal principal = authService.getCurrentPrincipal();
        notificationService.markAllAsRead(principal.getId());
        return ResponseEntity.noContent().build();
    }
}
