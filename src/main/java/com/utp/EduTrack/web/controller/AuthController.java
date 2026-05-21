package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.config.OpenApiConfig;
import com.utp.EduTrack.domain.dto.LoginRequestDTO;
import com.utp.EduTrack.domain.dto.LoginResponseDTO;
import com.utp.EduTrack.domain.dto.UserDTO;
import com.utp.EduTrack.domain.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "RF02 - Login y sesión actual")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = """
                    No requiere Authorize. Devuelve JWT según el rol (ADMIN, TEACHER, STUDENT).
                    Luego use el token en Authorize: Bearer {token}
                    """
    )
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
    @Operation(summary = "Usuario autenticado actual", description = "Verifica que el token JWT sea válido y muestra su rol")
    public ResponseEntity<UserDTO> me() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}
