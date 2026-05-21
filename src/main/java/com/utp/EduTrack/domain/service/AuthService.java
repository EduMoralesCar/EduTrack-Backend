package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.LoginRequestDTO;
import com.utp.EduTrack.domain.dto.LoginResponseDTO;
import com.utp.EduTrack.domain.dto.UserDTO;
import com.utp.EduTrack.domain.mapper.UserMapper;
import com.utp.EduTrack.security.JwtService;
import com.utp.EduTrack.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;

    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);

        return LoginResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(principal.getId())
                .username(principal.getUsername())
                .role(principal.getRole())
                .build();
    }

    public UserDTO getCurrentUser() {
        UserPrincipal principal = getCurrentPrincipal();
        return userMapper.toDto(userService.getUserEntity(principal.getId()));
    }

    public UserPrincipal getCurrentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException(
                    "Usuario no autenticado"
            );
        }
        return principal;
    }
}
