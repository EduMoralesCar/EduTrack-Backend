# Guía 05: Sincronización de la Aplicación Móvil (Android/iOS)

Esta guía detalla los pasos para sincronizar tu aplicación móvil nativa con el nuevo backend en la nube para que puedas generar el instalador (.apk) y probar el funcionamiento desde un celular.

---

## 1. Funcionamiento de la API en Entornos Nativos
Las aplicaciones nativas (móviles) no pueden usar rutas relativas como `/api`. Necesitan apuntar de forma explícita a la dirección web completa de tu backend en producción.

Por defecto, la aplicación utiliza una URL base fallida si no se le inyectan variables. Para facilitar el desarrollo y despliegue, configuramos que lea una URL por defecto en producción o una variable de entorno.

---

## 2. Configurar la URL Base del Servidor (Local)

1.  Abre el archivo de configuración del frontend: **`src/services/api.js`**.
2.  Busca la función **`getBaseUrl()`** y actualiza el valor de `prodDefault` con la IP o dominio de tu servidor web en AWS:
    ```javascript
    // Código en api.js
    const prodDefault = 'http://TU_IP_PUBLICA_AWS'; // Cambiar por tu IP o dominio de AWS
    ```
3.  Busca la función **`getFileUrl()`** y actualiza la línea correspondiente:
    ```javascript
    } else if (!import.meta.env.DEV) {
      base = 'http://TU_IP_PUBLICA_AWS/'; // Cambiar por tu IP o dominio de AWS
    }
    ```

---

## 3. Comandos de Compilación y Sincronización Móvil

Una vez configurada la URL en tu computadora local, ejecuta los siguientes comandos en la terminal de tu computadora (dentro de la carpeta `Frontend`):

```bash
# 1. Compilar el proyecto React (Vite)
npm run build

# 2. Copiar los nuevos archivos compilados al directorio nativo de Capacitor (Android/iOS)
npx cap sync
```

---

## 4. Despliegue en el Dispositivo Móvil

1.  **Abrir el entorno nativo:**
    *   Para Android: Abre el software **Android Studio**.
    *   Para iOS: Abre **Xcode** (solo disponible en Mac).
2.  **Sincronizar y Ejecutar:**
    *   En Android Studio, asegúrate de que reconozca los cambios.
    *   Conecta tu celular físico mediante cable USB (con la depuración USB activada) o enciende un emulador.
    *   Haz clic en el botón verde **"Run app"** (o ejecuta `npx cap run android`).
3.  **Generar el APK para distribución:**
    *   En el menú superior de Android Studio, ve a: **Build** ➡️ **Build Bundle(s) / APK(s)** ➡️ **Build APK(s)**.
    *   Una vez compilado, puedes pasar el archivo `.apk` a cualquier teléfono Android para probarlo directamente.
