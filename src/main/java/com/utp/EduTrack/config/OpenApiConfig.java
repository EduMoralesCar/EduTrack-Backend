package com.utp.EduTrack.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI edutrackOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("EduTrack API")
                        .version("1.0.0")
                        .description("""
                                ## Cómo probar en Swagger (avance RF01–RF05)

                                1. **POST /api/auth/login** (sin Authorize) con usuario y contraseña de tu BD.
                                2. Copia el campo `token` de la respuesta.
                                3. Pulsa **Authorize** (candado arriba) y pega el token directamente (NO escribas "Bearer", Swagger lo agrega automáticamente).
                                4. Usa los endpoints según tu rol.

                                ### Roles en este avance
                                | Rol | Permisos |
                                |-----|----------|
                                | **ADMIN** | Gestión completa: usuarios, cursos, secciones, matrículas |
                                | **TEACHER** | Solo lectura: cursos, secciones, matrículas de sus secciones |
                                | **STUDENT** | Solo lectura: cursos y secciones |

                                Los ejemplos JSON en cada endpoint muestran **todos los campos obligatorios**.
                                """))
                .components(new Components().addSecuritySchemes(
                        BEARER_AUTH,
                        new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token obtenido en POST /api/auth/login. Formato: Bearer {token}")
                ));
    }
}
