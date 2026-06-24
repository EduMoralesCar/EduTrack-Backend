package com.utp.EduTrack.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/health-db")
    public ResponseEntity<Map<String, Object>> checkDatabaseHealth() {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            response.put("status", "UP");
            response.put("database", "CONNECTED");
            response.put("test_query_result", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("database", "DISCONNECTED");
            response.put("error", e.getMessage());
            response.put("error_class", e.getClass().getName());
            return ResponseEntity.status(500).body(response);
        }
    }
}
