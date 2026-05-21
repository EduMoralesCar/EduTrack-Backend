package com.utp.EduTrack.domain.dto;

import com.utp.EduTrack.persistance.entity.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Matrícula de un estudiante en una sección")
public class EnrollmentDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "3")
    private Long studentId;

    @Schema(example = "estudiante1")
    private String studentUsername;

    @Schema(example = "1")
    private Long sectionId;

    @Schema(example = "A")
    private String sectionCode;

    @Schema(example = "2026-1")
    private String period;

    @Schema(example = "ENROLLED")
    private EnrollmentStatus status;
}
