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
        name = "UserUpdateRequest",
        description = "Datos para actualizar un usuario (RF01). password es opcional.",
        example = """
                {
                  "username": "docente1",
                  "email": "docente1@edutrack.local",
                  "role": "TEACHER",
                  "active": true
                }
                """
)
public class UserUpdateDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Nombre de usuario", example = "docente1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank
    @Email
    @Schema(description = "Correo electrónico", example = "docente1@edutrack.local", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotNull
    @Schema(description = "Rol del usuario", example = "TEACHER", requiredMode = Schema.RequiredMode.REQUIRED)
    private Role role;

    @NotNull
    @Schema(description = "Si el usuario puede iniciar sesión", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean active;

    @Size(min = 6, max = 100)
    @Schema(description = "Nueva contraseña (opcional; omitir si no se cambia)", example = "NuevaClave123!")
    private String password;
}
