package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(description = "Notificación o alerta académica")
public class NotificationDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long studentId;

    @Schema(example = "Se ha registrado una calificación de 15 en PC1")
    private String message;

    @Schema(example = "2026-07-06T16:00:00")
    private LocalDateTime createdAt;

    @Schema(example = "false")
    private boolean read;
}
