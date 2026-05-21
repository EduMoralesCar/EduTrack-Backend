package com.utp.EduTrack.web.handler;

import com.utp.EduTrack.domain.dto.ApiErrorDTO;
import com.utp.EduTrack.domain.exception.BusinessException;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorDTO> handleBusiness(BusinessException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorDTO> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales inválidas. Verifique usuario y contraseña.");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorDTO> handleAuthentication(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "No autenticado. Use POST /api/auth/login y luego Authorize con el token.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDTO> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN,
                "Acceso denegado. No posee los privilegios o el rol requerido para acceder a este recurso.");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiErrorDTO> handleDisabled(DisabledException ex) {
        return build(HttpStatus.FORBIDDEN, "Usuario inactivo");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, "Validación fallida: " + details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private ResponseEntity<ApiErrorDTO> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                ApiErrorDTO.builder()
                        .timestamp(Instant.now())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(message)
                        .build()
        );
    }
}
