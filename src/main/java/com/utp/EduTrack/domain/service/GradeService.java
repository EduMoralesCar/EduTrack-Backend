package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.GradeDTO;
import com.utp.EduTrack.domain.exception.BusinessException;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import com.utp.EduTrack.persistance.entity.Assignment;
import com.utp.EduTrack.persistance.entity.Grade;
import com.utp.EduTrack.persistance.entity.User;
import com.utp.EduTrack.persistance.repository.AssignmentRepository;
import com.utp.EduTrack.persistance.repository.GradeRepository;
import com.utp.EduTrack.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.utp.EduTrack.domain.dto.FinalGradeDTO;
import com.utp.EduTrack.persistance.entity.Enrollment;
import com.utp.EduTrack.persistance.repository.EnrollmentRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public GradeDTO recordGrade(GradeDTO dto) {
        if (dto.getScore() < 0.0 || dto.getScore() > 20.0) {
            throw new BusinessException("La nota debe estar entre 0 y 20.");
        }

        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Grade> grades = gradeRepository.findByAssignmentIdAndStudentId(dto.getAssignmentId(), dto.getStudentId());
        Grade grade;

        if (!grades.isEmpty()) {
            grade = grades.get(0);
            grade.setScore(dto.getScore());
            if (dto.getTeacherComment() != null) {
                grade.setTeacherComment(dto.getTeacherComment());
            }
            if (grades.size() > 1) {
                for (int i = 1; i < grades.size(); i++) {
                    gradeRepository.delete(grades.get(i));
                }
            }
        } else {
            grade = Grade.builder()
                    .assignment(assignment)
                    .student(student)
                    .score(dto.getScore())
                    .teacherComment(dto.getTeacherComment())
                    .build();
        }

        Grade saved = gradeRepository.save(grade);
        dto.setId(saved.getId());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<GradeDTO> getSectionGrades(Long sectionId) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth != null ? auth.getName() : "";
        boolean isStudent = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));

        List<Grade> grades = gradeRepository.findByAssignmentSectionId(sectionId);

        return grades.stream()
                .filter(g -> !isStudent || g.getStudent().getUsername().equalsIgnoreCase(currentUsername))
                .map(g -> GradeDTO.builder()
                        .id(g.getId())
                        .assignmentId(g.getAssignment().getId())
                        .studentId(g.getStudent().getId())
                        .score(g.getScore())
                        .teacherComment(g.getTeacherComment())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FinalGradeDTO> getSectionFinalGrades(Long sectionId) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth != null ? auth.getName() : "";
        boolean isStudent = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));

        List<Enrollment> enrollments = enrollmentRepository.findBySectionId(sectionId);
        if (isStudent) {
            enrollments = enrollments.stream()
                    .filter(e -> e.getStudent().getUsername().equalsIgnoreCase(currentUsername))
                    .collect(Collectors.toList());
        }

        List<Assignment> assignments = assignmentRepository.findBySectionId(sectionId);
        List<Grade> sectionGrades = gradeRepository.findByAssignmentSectionId(sectionId);
        
        List<FinalGradeDTO> result = new ArrayList<>();

        for (Enrollment enr : enrollments) {
            FinalGradeDTO finalGrade = FinalGradeDTO.builder()
                    .studentId(enr.getStudent().getId())
                    .studentUsername(enr.getStudent().getUsername())
                    .build();

            // Filter student grades from pre-fetched section grades
            List<Grade> studentGrades = sectionGrades.stream()
                    .filter(g -> g.getStudent().getId().equals(enr.getStudent().getId()))
                    .collect(Collectors.toList());

            // Map assignments to grades (using a merge function to prevent duplicate key exception)
            Map<Long, Double> gradeMap = studentGrades.stream()
                    .collect(Collectors.toMap(g -> g.getAssignment().getId(), Grade::getScore, (g1, g2) -> g2));

            double sumPA = 0.0;
            int countPA = 0;

            for (Assignment a : assignments) {
                Double score = gradeMap.get(a.getId());
                if (score != null) {
                    if ("PC1".equals(a.getCategory())) finalGrade.setPc1(score);
                    if ("PC2".equals(a.getCategory())) finalGrade.setPc2(score);
                    if ("PC3".equals(a.getCategory())) finalGrade.setPc3(score);
                    if ("EXFINAL".equals(a.getCategory())) finalGrade.setExfinal(score);
                    if ("PA".equals(a.getCategory())) {
                        sumPA += score;
                        countPA++;
                    }
                }
            }

            // Calculate PA Average
            if (countPA > 0) {
                finalGrade.setPa(sumPA / countPA);
            }

            // Calculate Final Average: weighted only on components that are actually graded (current weighted average)
            Double pc1 = finalGrade.getPc1();
            Double pc2 = finalGrade.getPc2();
            Double pc3 = finalGrade.getPc3();
            Double pa = finalGrade.getPa();
            Double exfinal = finalGrade.getExfinal();

            double weightedSum = 0.0;
            double totalWeight = 0.0;

            if (pc1 != null) {
                weightedSum += pc1 * 0.2;
                totalWeight += 0.2;
            }
            if (pc2 != null) {
                weightedSum += pc2 * 0.2;
                totalWeight += 0.2;
            }
            if (pc3 != null) {
                weightedSum += pc3 * 0.2;
                totalWeight += 0.2;
            }
            if (pa != null) {
                weightedSum += pa * 0.1;
                totalWeight += 0.1;
            }
            if (exfinal != null) {
                weightedSum += exfinal * 0.3;
                totalWeight += 0.3;
            }

            if (totalWeight > 0.0) {
                double finalAvg = weightedSum / totalWeight;
                finalGrade.setFinalAverage(Math.round(finalAvg * 100.0) / 100.0);
            } else {
                finalGrade.setFinalAverage(null);
            }

            result.add(finalGrade);
        }

        return result;
    }
}
