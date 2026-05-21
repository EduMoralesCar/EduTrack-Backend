package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(
        name = "AssignTeacherRequest",
        description = "Asignar docente a una sección (RF05). El usuario debe tener rol TEACHER.",
        example = """
                {
                  "teacherId": 2
                }
                """
)
public class AssignTeacherDTO {

    @NotNull
    @Schema(description = "ID del usuario docente (GET /api/users)", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long teacherId;
}
