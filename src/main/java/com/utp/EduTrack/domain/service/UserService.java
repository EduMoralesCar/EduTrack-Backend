package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.UserCreateDTO;
import com.utp.EduTrack.domain.dto.UserDTO;
import com.utp.EduTrack.domain.dto.UserUpdateDTO;
import com.utp.EduTrack.domain.exception.BusinessException;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import com.utp.EduTrack.domain.mapper.UserMapper;
import com.utp.EduTrack.persistance.entity.User;
import com.utp.EduTrack.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        return userMapper.toDto(getUserEntity(id));
    }

    public UserDTO create(UserCreateDTO request) {
        validateUniqueCredentials(request.getUsername(), request.getEmail(), null);

        User user = User.builder()
                .username(request.getUsername().trim())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        return userMapper.toDto(userRepository.save(user));
    }

    public UserDTO update(Long id, UserUpdateDTO request) {
        User user = getUserEntity(id);
        validateUniqueCredentials(request.getUsername(), request.getEmail(), id);

        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setRole(request.getRole());
        user.setActive(request.getActive());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userMapper.toDto(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User getUserEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    private void validateUniqueCredentials(String username, String email, Long excludeId) {
        boolean usernameExists = excludeId == null
                ? userRepository.existsByUsername(username)
                : userRepository.existsByUsernameAndIdNot(username, excludeId);

        boolean emailExists = excludeId == null
                ? userRepository.existsByEmail(email)
                : userRepository.existsByEmailAndIdNot(email, excludeId);

        if (usernameExists) {
            throw new BusinessException("El nombre de usuario ya está registrado");
        }
        if (emailExists) {
            throw new BusinessException("El correo electrónico ya está registrado");
        }
    }
}
