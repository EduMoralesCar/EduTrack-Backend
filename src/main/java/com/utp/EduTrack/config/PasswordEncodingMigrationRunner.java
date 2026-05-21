package com.utp.EduTrack.config;

import com.utp.EduTrack.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Convierte contraseñas en texto plano (datos viejos en BD) a BCrypt al arrancar.
 * No crea usuarios: los datos viven en PostgreSQL y se gestionan por la API o el cliente SQL.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordEncodingMigrationRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        long count = userRepository.count();
        log.info("==== DIAGNÓSTICO DE USUARIOS EN BASE DE DATOS (Total: {}) ====", count);
        userRepository.findAll().forEach(user -> {
            log.info("Usuario: id={}, username='{}', email='{}', role={}, active={}",
                    user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.isActive());
        });

        userRepository.findAll().stream()
                .filter(user -> !isBcryptEncoded(user.getPassword()))
                .forEach(user -> {
                    String plainPassword = user.getPassword();
                    user.setPassword(passwordEncoder.encode(plainPassword));
                    userRepository.save(user);
                    log.info("Contraseña migrada a BCrypt para usuario '{}'", user.getUsername());
                });
    }

    private boolean isBcryptEncoded(String password) {
        return password != null && password.startsWith("$2");
    }
}
