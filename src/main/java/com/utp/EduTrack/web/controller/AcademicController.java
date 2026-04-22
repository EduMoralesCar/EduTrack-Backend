package com.utp.EduTrack.web.controller;

import com.utp.EduTrack.persistance.entity.Assignment;
import com.utp.EduTrack.persistance.entity.Grade;
import com.utp.EduTrack.domain.service.AcademicEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AcademicController {

    private final AcademicEvaluationService academicService;

    @PostMapping("/sections/{sectionId}/assignments")
    public ResponseEntity<Assignment> createAssignment(@PathVariable Long sectionId, @RequestBody Assignment assignment) {
        // Typically you would map the sectionId to the assignment here
        return ResponseEntity.ok(academicService.createAssignment(assignment));
    }

    @GetMapping("/sections/{sectionId}/assignments")
    public ResponseEntity<List<Assignment>> getAssignmentsBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(academicService.getAssignmentsBySection(sectionId));
    }

    @PostMapping("/assignments/{assignmentId}/grades")
    public ResponseEntity<Grade> recordGrade(@PathVariable Long assignmentId, @RequestBody Grade grade) {
        return ResponseEntity.ok(academicService.recordGrade(grade));
    }

    @GetMapping("/students/{studentId}/grades")
    public ResponseEntity<List<Grade>> getGradesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(academicService.getGradesByStudent(studentId));
    }
    
    @GetMapping("/sections/{sectionId}/average/{studentId}")
    public ResponseEntity<Double> getStudentAverage(@PathVariable Long sectionId, @PathVariable Long studentId) {
        return ResponseEntity.ok(academicService.calculateSectionAverageForStudent(sectionId, studentId));
    }
}
