package com.utp.EduTrack.persistance.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Rol del usuario en el sistema", example = "ADMIN")
public enum Role {
    @Schema(description = "Estudiante")
    STUDENT,
    @Schema(description = "Docente")
    TEACHER,
    @Schema(description = "Administrador")
    ADMIN
}
