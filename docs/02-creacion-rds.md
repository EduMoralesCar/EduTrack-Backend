# Guía 02: Creación de la Base de Datos PostgreSQL en AWS RDS

Esta guía explica detalladamente cómo crear e inspeccionar una base de datos relacional PostgreSQL de forma gratuita utilizando el servicio **RDS** (Relational Database Service) de AWS.

---

## 1. Configuración del Asistente de Creación

1.  En la barra de búsqueda superior de la consola de AWS, escribe **RDS** y haz clic en el primer resultado.
2.  En el panel de control de RDS, haz clic en el botón blanco **"Create database"** (con fondo blanco y texto azul) ubicado bajo la tarjeta **"Create with full configuration"**.
    *   *Nota: No utilices la configuración rápida (Express/Easy create) ya que te creará recursos de cobro.*
3.  Configura las siguientes opciones exactamente como se detalla:

| Parámetro | Configuración Sugerida | Notas |
| :--- | :--- | :--- |
| **Database creation method** | `Standard create` | Habilita todas las opciones avanzadas |
| **Engine options** | `PostgreSQL` | Icono del elefante azul |
| **Engine version** | `PostgreSQL 16.x` | Versión estable compatible |
| **Templates** | `Free Tier` | **Obligatorio** para asegurar costo $0 USD |
| **DB instance identifier** | `edutrack-db` | Nombre identificador en AWS |
| **Master username** | `postgres` | Usuario administrador |
| **Credentials management** | `Self managed` | Controlarás tu propia clave |
| **Master password** | *Ingresa tu contraseña segura* | Guárdala bien |

---

## 2. Configuración de Hardware y Conectividad (¡Importante!)

*   **Instance configuration:** Deja por defecto `db.t4g.micro` o `db.t3.micro` (marcado como Capa Gratuita).
*   **Storage:** Selecciona `General Purpose SSD (gp2)` con **20 GiB** (límite de la capa gratuita).
*   **Connectivity (Conectividad):**
    *   **Compute resource:** Selecciona *Don't connect to an EC2 compute resource*.
    *   **Public access:** Selecciona **`Yes`**. *Esto es fundamental para conectarse desde tu computadora o desde el servidor web Nginx.*
    *   **VPC security group:** Selecciona **`Create new`** y asígnale el nombre `edutrack-db-sg`.
*   **Additional Configuration (Configuración Adicional - Al Final):**
    *   Despliega esta sección al final de la página.
    *   En **Initial database name**, escribe **`edutrack`** (en minúsculas). Esto creará automáticamente la base de datos interna con ese nombre.
4.  Haz clic en **"Create database"**. El estado pasará a `Creating` y tardará de 3 a 5 minutos en estar `Available`.

---

## 3. Apertura de Puertos (Firewall / Security Group)

Para permitir que el servidor web o tu computadora se conecten, debes abrir el puerto **5432** al público:

1.  Entra a los detalles de tu base de datos haciendo clic en su nombre (`edutrack-db`).
2.  Bajo la pestaña **Connectivity & security**, haz clic en el enlace azul del grupo de seguridad bajo **VPC security groups** (ej. `edutrack-db-sg (sg-...)`).
3.  En la pestaña inferior, ve a **Inbound rules** (Reglas de entrada) y haz clic en **Edit inbound rules**.
4.  Haz clic en **Add rule** y configúrala:
    *   **Type:** `PostgreSQL` (Puerto 5432).
    *   **Source:** `Anywhere-IPv4` (`0.0.0.0/0`).
5.  Haz clic en **Save rules**.

---

## 4. Obtención de Credenciales de Conexión (Ejemplo)

Una vez creada la base de datos, en la pestaña **Connectivity & security** obtendrás el **Endpoint** para conectarte:

*   **Host (Endpoint):** `TU_BD_IDENTIFIER.TU_CODIGO_ALEATORIO.REGION.rds.amazonaws.com`
*   **Port:** `5432`
*   **User:** `postgres`
*   **Password:** `TU_CONTRASEÑA`
*   **Database:** `edutrack`

Puedes ingresar esta información en clientes gráficos locales como **DBeaver** o **pgAdmin** para explorar e interactuar de forma gráfica con las tablas.
