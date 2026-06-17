package com.utp.EduTrack.domain.dto;

import com.utp.EduTrack.persistance.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(description = "Respuesta de login con token JWT y datos del usuario")
public class LoginResponseDTO {

    @Schema(description = "Token JWT. Copiar y usar en Authorize como: Bearer {token}", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private String tokenType;

    @Schema(description = "ID del usuario en la BD", example = "1")
    private Long userId;

    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;

    @Schema(description = "Rol del usuario autenticado", example = "ADMIN")
    private Role role;
}

