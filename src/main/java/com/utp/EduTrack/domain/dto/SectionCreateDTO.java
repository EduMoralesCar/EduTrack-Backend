package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "SectionCreateRequest",
        description = "Crear sección por curso y periodo (RF04). El docente se asigna después con PUT /sections/{id}/teacher",
        example = """
                {
                  "courseId": 1,
                  "period": "2026-1",
                  "code": "A"
                }
                """
)
public class SectionCreateDTO {

    @NotNull
    @Schema(description = "ID del curso (obtener de GET /api/courses)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long courseId;

    @NotBlank
    @Size(max = 20)
    @Schema(description = "Periodo académico", example = "2026-1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String period;

    @NotBlank
    @Size(max = 10)
    @Schema(description = "Código de sección dentro del curso", example = "A", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
}
