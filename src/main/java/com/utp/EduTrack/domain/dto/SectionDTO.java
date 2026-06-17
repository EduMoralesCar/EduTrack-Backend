package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(description = "Sección de un curso")
public class SectionDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long courseId;

    @Schema(example = "Programación I")
    private String courseName;

    @Schema(example = "PROG101")
    private String courseCode;

    @Schema(description = "ID del docente (null si aún no asignado)", example = "2")
    private Long teacherId;

    @Schema(description = "Username del docente", example = "docente1")
    private String teacherUsername;

    @Schema(example = "2026-1")
    private String period;

    @Schema(example = "A")
    private String code;
}

