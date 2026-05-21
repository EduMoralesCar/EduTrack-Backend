package com.utp.EduTrack.domain.dto;

import com.utp.EduTrack.persistance.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "UserCreateRequest",
        description = "Datos para registrar un usuario (RF01). Solo ADMIN.",
        example = """
                {
                  "username": "docente1",
                  "password": "Docente123!",
                  "email": "docente1@edutrack.local",
                  "role": "TEACHER"
                }
                """
)
public class UserCreateDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Nombre de usuario único", example = "docente1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    @Schema(description = "Contraseña (mínimo 6 caracteres)", example = "Docente123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank
    @Email
    @Schema(description = "Correo electrónico único", example = "docente1@edutrack.local", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotNull
    @Schema(description = "Rol: ADMIN, TEACHER o STUDENT", example = "TEACHER", requiredMode = Schema.RequiredMode.REQUIRED)
    private Role role;
}
