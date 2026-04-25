# EduTrack - Sistema Integral de Gestión Académica (SIGA)

EduTrack es un sistema de control académico desarrollado con **Spring Boot**, diseñado para gestionar operaciones educativas. Permite administrar de forma centralizada la información académica (estudiantes, profesores, cursos, etc.) con una arquitectura MVC y N-capas.

## 🚀 Tecnologías Utilizadas

* **Java 21**
* **Spring Boot 4.x** (WebMVC, Data JPA)
* **PostgreSQL** (Base de datos relacional)
* **Lombok** (Reducción de código repetitivo)
* **Maven** (Gestión de dependencias)

## 📋 Requisitos Previos

Para ejecutar el proyecto en tu entorno local, necesitas tener instalado:

1. [Java Development Kit (JDK) 21](https://jdk.java.net/21/)
2. [Maven](https://maven.apache.org/) (Opcional, el proyecto incluye Maven Wrapper `mvnw`)
3. [PostgreSQL](https://www.postgresql.org/download/)

## ⚙️ Configuración y Ejecución Local

### 1. Configurar la Base de Datos

Crea una base de datos en PostgreSQL con el nombre `edutrack`.
Puedes hacerlo mediante pgAdmin, DBeaver o la línea de comandos de psql:

```sql
CREATE DATABASE edutrack;
```

Asegúrate de que tus credenciales de PostgreSQL coincidan con las configuradas en `src/main/resources/application.properties`. Por defecto están así:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/edutrack
spring.datasource.username=postgres
spring.datasource.password=admin
```
*(Si tu usuario o contraseña son diferentes, actualiza el archivo `application.properties` antes de ejecutar el proyecto).*

### 2. Levantar el Proyecto

Abre una terminal en la raíz del proyecto y ejecuta el siguiente comando:

**En Windows (PowerShell/CMD):**
```bash
.\mvnw spring-boot:run
```

**En Linux/Mac:**
```bash
./mvnw spring-boot:run
```

El servidor iniciará por defecto en el puerto `8080` (a menos que se especifique otro en properties). 
Verás los logs en la consola indicando que la aplicación ha iniciado y que las tablas de la base de datos se han creado automáticamente gracias a la propiedad `spring.jpa.hibernate.ddl-auto=update`.

## 📁 Estructura del Proyecto

El proyecto sigue una arquitectura multicapa, promoviendo la separación de responsabilidades:
* **Controllers (`@Controller` / `@RestController`)**: Manejan las peticiones HTTP.
* **Services (`@Service`)**: Contienen la lógica de negocio.
* **Repositories (`@Repository`)**: Gestionan el acceso a datos mediante Spring Data JPA.
* **Entities (`@Entity`)**: Representan las tablas de la base de datos.
