package com.utp.EduTrack.controller;

import com.utp.EduTrack.dto.UserCreateDTO;
import com.utp.EduTrack.dto.UserDTO;
import com.utp.EduTrack.entity.User;
import com.utp.EduTrack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Convierte Entidad a DTO para ocultar contraseñas
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

    // 1. Crear (POST)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO createDTO) {
        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setPassword(createDTO.getPassword());
        user.setEmail(createDTO.getEmail());
        user.setRole(createDTO.getRole());
        user.setActive(true);

        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(convertToDTO(savedUser));
    }

    // 2. Obtener todos (GET)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // 3. Obtener por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(convertToDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Actualizar (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserCreateDTO updateDTO) {
        User userUpdate = new User();
        userUpdate.setUsername(updateDTO.getUsername());
        userUpdate.setEmail(updateDTO.getEmail());
        userUpdate.setRole(updateDTO.getRole());
        userUpdate.setActive(true); 

        User updatedUser = userService.updateUser(id, userUpdate);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }

    // 5. Eliminar (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
