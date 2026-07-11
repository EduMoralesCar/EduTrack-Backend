# Guía 04: Configuración del Servidor y Despliegue de la Aplicación

Esta guía técnica describe de forma ordenada todos los comandos ejecutados en el servidor Linux Ubuntu para configurar el entorno e implementar el frontend (React) y backend (Spring Boot).

---

## Paso 1: Actualización del Sistema y Creación del SWAP
Para evitar que un servidor pequeño de 1 GB de RAM aborte la compilación por falta de memoria (OOM), creamos un espacio de intercambio (Swap) de 2 GB:

```bash
# Actualizar repositorios e instalar actualizaciones del sistema
sudo apt update && sudo apt upgrade -y

# Crear archivo swap de 2GB
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
```

---

## Paso 2: Instalación de Herramientas del Sistema
Instalamos el compilador de Java (JDK 21), Node.js/NPM para React, el servidor Nginx y el cliente Git:

```bash
sudo apt install -y openjdk-21-jdk nodejs npm nginx git curl postgresql-client
```

---

## Paso 3: Clonación y Construcción del Frontend (React)

```bash
# Clonar repositorio de frontend
git clone https://github.com/TU_ORGANIZACION/EduTrack-Frontend.git
cd EduTrack-Frontend
npm install

# Compilar indicando la dirección IP pública de tu servidor backend de AWS
VITE_API_URL=http://TU_IP_PUBLICA_AWS npm run build

# Mover archivos compilados al directorio público del servidor
sudo mkdir -p /var/www/edutrack
sudo cp -r dist/* /var/www/edutrack/
```

---

## Paso 4: Configuración de Nginx (Servidor Web y Proxy Inverso)
Reemplazamos el archivo `/etc/nginx/sites-available/default` para que Nginx sirva los archivos de React y redirija las llamadas a la API `/api` al puerto local `8081` de Spring Boot:

```bash
sudo tee /etc/nginx/sites-available/default << 'EOF'
server {
    listen 80;
    server_name _;

    root /var/www/edutrack;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
EOF

# Validar sintaxis y reiniciar Nginx
sudo nginx -t
sudo systemctl restart nginx
```

---

## Paso 5: Construcción y Servicio del Backend (Spring Boot)

1.  **Clonar y Compilar el Backend:**
    ```bash
    cd /home/ubuntu
    git clone https://github.com/TU_ORGANIZACION/EduTrack-Backend.git
    cd EduTrack-Backend
    chmod +x ./mvnw
    ./mvnw clean package -DskipTests
    ```

2.  **Crear el servicio de fondo (systemd):**
    Creamos un servicio para que el backend corra permanentemente como proceso del sistema operativo:
    ```bash
    sudo tee /etc/systemd/system/edutrack.service << 'EOF'
    [Unit]
    Description=EduTrack Backend Service
    After=syslog.target network.target

    [Service]
    User=ubuntu
    WorkingDirectory=/home/ubuntu/EduTrack-Backend
    Environment="SPRING_DATASOURCE_URL=jdbc:postgresql://TU_RDS_ENDPOINT:5432/edutrack"
    Environment="SPRING_DATASOURCE_USERNAME=postgres"
    Environment="SPRING_DATASOURCE_PASSWORD=TU_RDS_PASSWORD"
    Environment="PORT=8081"
    ExecStart=/usr/bin/java -jar /home/ubuntu/EduTrack-Backend/target/EduTrack-0.0.1-SNAPSHOT.jar
    SuccessExitStatus=143
    Restart=always
    RestartSec=10

    [Install]
    WantedBy=multi-user.target
    EOF
    ```

3.  **Iniciar y Habilitar el Servicio:**
    ```bash
    sudo systemctl daemon-reload
    sudo systemctl start edutrack
    sudo systemctl enable edutrack
    ```

4.  **Comandos útiles de mantenimiento:**
    *   **Ver logs de arranque:** `sudo journalctl -u edutrack -n 50 --no-pager`
    *   **Ver estado actual:** `sudo systemctl status edutrack`
    *   **Reiniciar backend:** `sudo systemctl restart edutrack`
