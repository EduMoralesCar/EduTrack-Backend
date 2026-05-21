package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(
        name = "EnrollmentCreateRequest",
        description = "Matricular estudiante en una sección (RF04). El usuario debe tener rol STUDENT.",
        example = """
                {
                  "studentId": 3,
                  "sectionId": 1
                }
                """
)
public class EnrollmentCreateDTO {

    @NotNull
    @Schema(description = "ID del estudiante (GET /api/users)", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long studentId;

    @NotNull
    @Schema(description = "ID de la sección (GET /api/sections)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sectionId;
}
