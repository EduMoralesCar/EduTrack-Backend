package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.persistance.entity.Assignment;
import com.utp.EduTrack.persistance.entity.Grade;
import com.utp.EduTrack.domain.repository.AssignmentRepository;
import com.utp.EduTrack.domain.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicEvaluationService {

    private final AssignmentRepository assignmentRepository;
    private final GradeRepository gradeRepository;

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAssignmentsBySection(Long sectionId) {
        return assignmentRepository.findBySectionId(sectionId);
    }

    public Grade recordGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public Double calculateSectionAverageForStudent(Long sectionId, Long studentId) {
        // Business logic to calculate average dynamically using weights
        return 0.0; // Placeholder
    }
}
