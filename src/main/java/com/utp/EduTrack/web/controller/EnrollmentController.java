package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.persistance.entity.Enrollment;
import com.utp.EduTrack.domain.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsBySection(sectionId));
    }

    @PostMapping
    public ResponseEntity<Enrollment> enrollStudent(@RequestBody Enrollment enrollment) {
        return ResponseEntity.ok(enrollmentService.enrollStudent(enrollment));
    }
}
