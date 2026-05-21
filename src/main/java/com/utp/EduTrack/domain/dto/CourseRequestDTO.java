package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "CourseRequest",
        description = "Datos para crear o actualizar un curso (RF03)",
        example = """
                {
                  "name": "Programación I",
                  "code": "PROG101",
                  "description": "Fundamentos de programación",
                  "credits": 4
                }
                """
)
public class CourseRequestDTO {

    @NotBlank
    @Size(max = 150)
    @Schema(description = "Nombre del curso", example = "Programación I", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Size(max = 20)
    @Schema(description = "Código único del curso", example = "PROG101", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @Size(max = 500)
    @Schema(description = "Descripción del curso", example = "Fundamentos de programación")
    private String description;

    @NotNull
    @Positive
    @Schema(description = "Número de créditos", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer credits;
}
