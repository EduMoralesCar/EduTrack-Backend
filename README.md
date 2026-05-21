<div align="center">
  
# 🎓 EduTrack - Backend API
### Control Académico Centralizado - Entrega Actual (RF & RNF)

<br/>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

</div>

---

## 📋 Requerimientos Funcionales Entregados (RF)

Esta entrega cubre la automatización y control administrativo de accesos, cursos, secciones y matrículas académicas:

*   **RF01 - Gestión de Usuarios:**
    *   *Descripción:* CRUD administrativo para control de cuentas académicas.
    *   *Endpoint base:* `GET | POST | PUT | DELETE /api/users`
    *   *Permisos:* Restringido exclusivamente al rol `ADMIN`.
*   **RF02 - Autenticación y Sesión:**
    *   *Descripción:* Inicio de sesión seguro y validación de perfiles activos.
    *   *Endpoints:* `POST /api/auth/login` (público) y `GET /api/auth/me` (requiere token).
    *   *Permisos:* Público para login; lectura protegida para cualquier usuario autenticado.
*   **RF03 - Gestión de Cursos (Catálogo):**
    *   *Descripción:* CRUD completo de materias (código, créditos, silabo).
    *   *Endpoint base:* `GET | POST | PUT | DELETE /api/courses`
    *   *Permisos:* Escritura (`POST/PUT/DELETE`) para `ADMIN`. Lectura general (`GET`) para `ADMIN`, `TEACHER` y `STUDENT`.
*   **RF04 - Secciones y Matrícula Estudiantil:**
    *   *Descripción:* Apertura de secciones por período y matrícula de alumnos en aulas virtuales.
    *   *Endpoints:* 
        *   Secciones: `GET | POST | PUT /api/sections`
        *   Matrículas: `GET /api/enrollments/section/{id}` y `POST | DELETE /api/enrollments`
    *   *Permisos:* Matrícula activa restringida a `ADMIN`. Lectura de padrones para `ADMIN` y `TEACHER`.
*   **RF05 - Asignación Docente:**
    *   *Descripción:* Vinculación de profesores titulares a secciones específicas.
    *   *Endpoint:* `PUT /api/sections/{id}/teacher`
    *   *Permisos:* Restringido exclusivamente al rol `ADMIN`.

---

## 🔒 Requerimientos No Funcionales Clave (RNF)

*   **RNF02 - Control de Acceso por Roles (RBAC):**
    *   Seguridad basada en metodologías JWT robustas. Anotaciones `@PreAuthorize` en controladores controlando los privilegios de los perfiles `ADMIN`, `TEACHER` y `STUDENT`.
*   **RNF06 - Arquitectura DTO (Data Transfer Objects):**
    *   Aislamiento total del modelo de datos de la base de datos hacia el exterior. Mappings de negocio eficientes que previenen overhead de red y exposición accidental de datos sensibles.
*   **RNF07 - Integración y Persistencia (PostgreSQL):**
    *   Integridad referencial a nivel de base de datos relacional PostgreSQL con constraints estrictos de claves foráneas y cascadas administradas por Hibernate.

---

## 🛠️ Ejecución y Pruebas Rápidas

1.  **Levantar el Servidor:**
    ```powershell
    .\mvnw spring-boot:run
    ```
    *(Activo en el puerto `8081` para evitar colisiones locales).*
2.  **Verificación de API (Swagger UI):**
    *   [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
3.  **Cuentas de Bootstrap Integradas:**
    *   🔑 **Administrador:** `JUAN PABLO` / `Admin123!`
    *   🔑 **Docente:** `FERMIN LOPEZ` / `Docente123!`
    *   🔑 **Estudiante:** `EDU MORALES` / `Estudiante123!`
