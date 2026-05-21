package com.utp.EduTrack.domain.dto;

import com.utp.EduTrack.persistance.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Usuario (sin contraseña)")
public class UserDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "admin")
    private String username;

    @Schema(example = "admin@edutrack.local")
    private String email;

    @Schema(example = "ADMIN")
    private Role role;

    @Schema(description = "Usuario activo", example = "true")
    private boolean active;
}
