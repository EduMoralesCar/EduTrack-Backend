# Guía 03: Creación y Red de la Instancia AWS EC2 (Servidor)

Esta guía detalla el proceso para lanzar una máquina virtual Linux en AWS EC2 (Elastic Compute Cloud) para servir como el servidor web de frontend (React) y backend (Spring Boot).

---

## 1. Lanzamiento de la Instancia EC2

1.  En la barra de búsqueda de AWS, escribe **EC2** y haz clic en el primer resultado.
2.  En el panel de EC2, haz clic en **"Launch instance"** (Lanzar instancia).
3.  Configura el formulario exactamente con estos parámetros:

| Sección | Parámetro | Configuración Sugerida | Notas |
| :--- | :--- | :--- | :--- |
| **Name and tags** | `Name` | `edutrack-server` | Nombre identificador |
| **OS Images** | `Quick Start` | `Ubuntu` | Distribución Linux estándar |
| **OS Version** | `Amazon Machine Image` | `Ubuntu Server 24.04 LTS (HVM)` | Debe decir **Free tier eligible** |
| **Instance type** | `Instance type` | `t2.micro` o `t3.micro` | Debe decir **Free tier eligible** |
| **Key pair** | `Key pair (login)` | *Crear nueva clave* | RSA, formato `.pem` (guarda el archivo en tu PC) |

---

## 2. Configuración de Red y Seguridad (Firewall)

En la sección **Network settings** (Configuración de red), mantén marcadas las siguientes casillas:

*   `Allow SSH traffic from` ➡️ **Anywhere** (Cualquier lugar: `0.0.0.0/0`). Permite conectarte por consola de forma segura.
*   `Allow HTTPS traffic from the internet` ➡️ **Marcado**. Permite tráfico web seguro (Puerto 443).
*   `Allow HTTP traffic from the internet` ➡️ **Marcado**. Permite tráfico web estándar (Puerto 80).

Haz clic en el botón naranja **"Launch instance"**. La máquina se encenderá en aproximadamente 1 minuto.

---

## 3. Identificación de Direcciones IP (Ejemplo)

Una vez creada, selecciona tu instancia en la tabla de EC2 para ver sus detalles en el panel inferior:

*   **Public IPv4 Address (IP Pública):** `TU_IP_PUBLICA_AWS` (por ejemplo, `13.59.189.231`).
    *   *Esta es la IP que los usuarios escribirán en el navegador para ingresar a la web.*
*   **Private IPv4 Address (IP Privada):** `TU_IP_PRIVADA` (usada internamente por AWS).
*   **Security group:** Por defecto se creará un grupo con un nombre como `launch-wizard-1`.

---

## 4. Conexión Rápida por el Navegador (EC2 Instance Connect)

No necesitas instalar programas complejos (como PuTTY o configurar SSH localmente) para administrar el servidor. AWS te da una consola web integrada:

1.  En la tabla de instancias de EC2, selecciona tu servidor marcando la casilla de la izquierda.
2.  Haz clic en el botón **"Connect"** (Conectar) en la parte superior.
3.  En la pestaña **EC2 Instance Connect**, deja el nombre de usuario por defecto (`ubuntu`).
4.  Haz clic en el botón naranja **"Connect"**.
5.  Se abrirá una nueva pestaña con una terminal negra de Linux lista para ingresar comandos.
