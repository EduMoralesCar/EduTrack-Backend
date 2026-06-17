package com.utp.EduTrack.domain.dto;

import com.utp.EduTrack.persistance.entity.AssignmentType;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AssignmentDTO {
    private Long id;
    private String name;

    @Schema(description = "Tipo de evaluación", example = "TAREA")
    private AssignmentType type;

    @Schema(example = "2026-06-15T08:30:00")
    @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @Schema(example = "2026-06-20T23:59:00")
    @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    @Schema(example = "5")
    private Integer weekNumber;

    @Schema(example = "PC1")
    private String category;

    private String description;
    private String instructionsFilePath;

    @Schema(example = "1")
    private Long sectionId;
}
