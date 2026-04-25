<div align="center">
  
# 🎓 EduTrack
### Sistema Integral de Gestión Académica (SIGA)

<br/>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

</div>

<br/>

## 📝 Sobre el Proyecto

**EduTrack** es una plataforma moderna de control académico desarrollada para gestionar todas las operaciones educativas de una institución. Permite administrar de forma centralizada la información académica (estudiantes, profesores, cursos, asistencias y calificaciones) asegurando un rendimiento óptimo gracias a su arquitectura limpia y escalable.

---

## 🏗️ Arquitectura del Sistema

El proyecto sigue una **arquitectura N-capas** basada en los principios de diseño de Spring Boot, lo que garantiza bajo acoplamiento y alta cohesión:

- 🌐 **Web (`/web/controller`)**: Controladores REST y endpoints HTTP.
- ⚙️ **Domain (`/domain/service`)**: Reglas y lógica de negocio principal.
- 📦 **Domain (`/domain/dto`)**: Objetos de transferencia de datos.
- 💾 **Persistance (`/domain/repository`)**: Interfaces de acceso a datos de Spring Data JPA.
- 🗄️ **Persistance (`/persistance/entity`)**: Modelos que mapean las tablas en PostgreSQL.

---

## 🚀 Guía de Instalación Rápida

Sigue estos pasos para levantar el entorno de desarrollo en tu máquina local.

### 1️⃣ Requisitos Previos

Asegúrate de tener instaladas las siguientes herramientas:

* ☕ [JDK 21](https://jdk.java.net/21/)
* 🐘 [PostgreSQL](https://www.postgresql.org/download/)
* 🧩 Un IDE moderno (IntelliJ IDEA, VS Code, o Eclipse)

### 2️⃣ Configuración de Base de Datos

Abre tu gestor de base de datos preferido (DBeaver, pgAdmin, o consola `psql`) y ejecuta:

```sql
CREATE DATABASE edutrack;
```

> [!IMPORTANT]  
> **Credenciales por defecto:** El proyecto buscará por defecto el usuario `postgres` y la contraseña `admin` en el puerto `5432`. Puedes modificarlas en el archivo `src/main/resources/application.properties` si tu configuración local es distinta.

### 3️⃣ Iniciar la Aplicación

Abre tu terminal en la carpeta principal del proyecto y ejecuta el *Maven Wrapper*:

**En Windows:**
```powershell
.\mvnw spring-boot:run
```

**En Linux / macOS:**
```bash
./mvnw spring-boot:run
```

<br/>

> [!TIP]
> 💡 **Auto-creación de tablas:** Una vez iniciado, Spring Boot creará automáticamente todas las tablas relacionales en la base de datos gracias a la configuración de `hibernate.ddl-auto=update`.

---
<div align="center">
  <p>Construido con ❤️ para la gestión académica</p>
</div>
