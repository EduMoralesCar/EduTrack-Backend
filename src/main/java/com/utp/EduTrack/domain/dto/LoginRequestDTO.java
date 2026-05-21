package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        name = "LoginRequest",
        description = "Credenciales para iniciar sesión (RF02)",
        example = """
                {
                  "username": "admin",
                  "password": "Admin123!"
                }
                """
)
public class LoginRequestDTO {

    @NotBlank
    @Schema(description = "Nombre de usuario registrado en la BD", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank
    @Schema(description = "Contraseña del usuario", example = "Admin123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
