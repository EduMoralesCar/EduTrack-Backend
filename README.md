<div align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger" />
</div>

<h1 align="center">🎓 EduTrack - API Backend (Gestión Académica)</h1>

<p align="center">
  API RESTful robusta, segura y escalable para la administración centralizada de la plataforma educativa EduTrack.
</p>

<div align="center">
  <h3>
    <a href="https://edutrack-backend-1nvs.onrender.com/swagger-ui/index.html#/Evaluaciones%20(Assignments)">🚀 VER DOCUMENTACIÓN INTERACTIVA (SWAGGER UI)</a>
  </h3>
</div>

---

## 🌟 Sobre el Proyecto

El backend de **EduTrack** es el motor de datos y lógica de negocio de la plataforma académica. Está diseñado bajo una arquitectura limpia en tres capas (Controller, Service, Repository) utilizando **Spring Boot 3.x** y **Spring Security**, asegurando una interconexión fluida y controlada mediante un estricto **Control de Acceso Basado en Roles (RBAC)**. Esto garantiza que Administradores, Docentes y Estudiantes solo accedan a la información y operaciones autorizadas para sus perfiles.

---

## ✨ Funcionalidades del Backend (Entregas Completas: Avance 1 - Avance 3)

El proyecto expone endpoints RESTful estructurados y protegidos mediante autenticación de **Tokens JWT** (JSON Web Tokens) firmados digitalmente, utilizando el patrón **DTO (Data Transfer Objects)** para evitar la exposición directa de entidades de base de datos relacional (**PostgreSQL**).

### 🔒 1. Seguridad, Sesión y Autenticación (Avance 1)
- **Inicio de Sesión Seguro (RF 02):** Autenticación en `POST /api/auth/login` con encriptación Bcrypt y generación de tokens JWT.
- **Perfil Activo:** Consulta del perfil actual del usuario autenticado en `GET /api/auth/me`.
- **RBAC Estricto:** Restricción perimetral de endpoints utilizando anotaciones `@PreAuthorize` según el rol (`ADMIN`, `TEACHER`, `STUDENT`).

### 👑 2. Panel Administrativo (Avance 1 & 2)
- **Gestión de Usuarios (RF 01):** CRUD completo (Creación, Lectura, Modificación, Baja) de perfiles de estudiantes, docentes y administradores (`/api/users`).
- **Catálogo de Cursos (RF 03):** CRUD total de asignaturas con código único, créditos y silabo curricular (`/api/courses`).
- **Gestión de Secciones (RF 04):** Apertura y parametrización de aulas virtuales por ciclo escolar (`/api/sections`).
- **Matrícula Estudiantil (RF 04):** Asignación y baja de alumnos en secciones académicas (`/api/enrollments`).
- **Asignación Docente (RF 05):** Vinculación de profesores titulares a sus respectivas secciones (`PUT /api/sections/{id}/teacher`).

### 👨‍🏫 3. Gestión Académica para Docentes (Avance 3)
- **Materiales de Clase (RF 06):** Carga, edición y eliminación de recursos en formato PDF, diapositivas y enlaces externos organizados por semana.
- **Gestión de Evaluaciones (RF 06):** Programación de tareas, prácticas de laboratorio y exámenes parciales/finales, definiendo fechas límite e intentos permitidos (`POST /api/assignments`).
- **Registro de Notas (RF 08):** Calificación numérica (de 0 a 20) de los trabajos entregados con cálculo automatizado en tiempo real del promedio ponderado oficial de la UTP.
- **Registro de Asistencias (RF 09):** Control diario de inasistencias y tardanzas (PRESENTE, AUSENTE, TARDE) por semana.
- **Resolución de Justificaciones (RF 10):** Bandeja para revisar, descargar comprobantes médicos o laborales y resolver (Aprobar/Rechazar) inasistencias solicitadas por estudiantes.

### 👨‍🎓 4. Portal del Estudiante (Avance 3)
- **Carga de Entregas (RF 07):** Envío digital de soluciones de tareas con validación dinámica del límite de intentos configurados (`maxAttempts`).
- **Récord de Calificaciones (RF 08):** Boleta de notas desglosada por categorías ponderadas (PA: 10%, PC1: 20%, PC2: 20%, PC3: 20%, EXFINAL: 30%) y estado final.
- **Control de Inasistencias (RF 09):** Indicador visual del récord de faltas y alerta de inhabilitación de matrícula si el porcentaje cae por debajo de la norma institucional (70%).
- **Justificación de Faltas (RF 10):** Solicitud de justificaciones para clases marcadas como `AUSENTE`, adjuntando comentarios y archivos de soporte.

---

## 🔑 Credenciales de Acceso (Pruebas en Swagger)

Para probar los endpoints en la ruta `/swagger-ui/index.html`, primero debes autenticarte en `POST /api/auth/login` con cualquiera de las siguientes cuentas de prueba. Luego, copia el token generado de la respuesta y agrégalo en el botón verde **"Authorize"** de Swagger precedido por la palabra `Bearer` (ejemplo: `Bearer eyJhbGci...`).

| Rol | Usuario | Contraseña |
| :--- | :--- | :--- |
| **Administrador** | `JUAN PABLO` | `Admin123!` |
| **Docente** | `FERMIN LOPEZ` | `Docente123!` |
| **Estudiante** | `EDU MORALES` | `Estudiante123!` |

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Java 17+
- **Framework Web:** Spring Boot 3.x
- **Persistencia & ORM:** Spring Data JPA & Hibernate
- **Base de Datos:** PostgreSQL
- **Seguridad:** Spring Security (Bcrypt & JWT)
- **Construcción y Dependencias:** Maven
- **Documentación:** Swagger UI (OpenAPI 3)
- **Despliegue:** Render

---

## 🚀 Despliegue Local (Para Desarrolladores)

Si deseas clonar el proyecto y correr el backend en tu máquina:

1. **Clonar el repositorio:**
```bash
git clone https://github.com/EduMoralesCar/EduTrack.git
cd EduTrack/Backend
```

2. **Configuración de Variables de Entorno:**
El proyecto cuenta con un archivo de configuración en `src/main/resources/application.properties` (o variables de entorno correspondientes). Asegúrate de configurar los accesos de tu base de datos PostgreSQL local o de desarrollo:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/edutrack
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
```

3. **Ejecutar el servidor local:**
Puedes usar Maven Wrapper desde la raíz de la carpeta `Backend`:
```powershell
# En Windows:
.\mvnw spring-boot:run
# En Mac/Linux:
./mvnw spring-boot:run
```
*Nota: El backend corre por defecto en el puerto `8081` para evitar colisiones locales.*

4. **Abrir la documentación:**
Navega a [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html) para interactuar con la API de forma visual.
