package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.AcademicReportDTO;
import com.utp.EduTrack.domain.dto.FinalGradeDTO;
import com.utp.EduTrack.persistance.entity.Attendance;
import com.utp.EduTrack.persistance.entity.AttendanceStatus;
import com.utp.EduTrack.persistance.entity.Enrollment;
import com.utp.EduTrack.persistance.entity.EnrollmentStatus;
import com.utp.EduTrack.persistance.entity.Section;
import com.utp.EduTrack.persistance.repository.AttendanceRepository;
import com.utp.EduTrack.persistance.repository.EnrollmentRepository;
import com.utp.EduTrack.persistance.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final EnrollmentRepository enrollmentRepository;
    private final SectionRepository sectionRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeService gradeService;

    public AcademicReportDTO getAcademicReport() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        
        long totalStudents = enrollments.stream().map(e -> e.getStudent().getId()).distinct().count();
        long activeCount = enrollments.stream().filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED).map(e -> e.getStudent().getId()).distinct().count();
        long riskCount = enrollments.stream().filter(e -> e.getStatus() == EnrollmentStatus.AT_RISK).map(e -> e.getStudent().getId()).distinct().count();
        long withdrawnCount = enrollments.stream().filter(e -> e.getStatus() == EnrollmentStatus.WITHDRAWN).map(e -> e.getStudent().getId()).distinct().count();

        // Overall attendance rate calculation
        List<Attendance> attendances = attendanceRepository.findAll();
        double overallAttendance = 1.0;
        if (!attendances.isEmpty()) {
            long present = attendances.stream()
                    .filter(a -> a.getStatus() == AttendanceStatus.PRESENTE 
                              || a.getStatus() == AttendanceStatus.TARDE 
                              || a.getStatus() == AttendanceStatus.JUSTIFICADO)
                    .count();
            overallAttendance = (double) present / attendances.size();
        }

        // Students at Risk details
        List<AcademicReportDTO.StudentRiskDTO> studentsAtRisk = new ArrayList<>();
        List<Enrollment> riskEnrollments = enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.AT_RISK)
                .toList();

        for (Enrollment enr : riskEnrollments) {
            Long studentId = enr.getStudent().getId();
            Long sectionId = enr.getSection().getId();

            // Calculate attendance
            List<Attendance> studentAttendances = attendanceRepository.findBySectionIdAndStudentId(sectionId, studentId);
            double attendanceRate = 1.0;
            if (!studentAttendances.isEmpty()) {
                long present = studentAttendances.stream()
                        .filter(a -> a.getStatus() == AttendanceStatus.PRESENTE 
                                  || a.getStatus() == AttendanceStatus.TARDE 
                                  || a.getStatus() == AttendanceStatus.JUSTIFICADO)
                        .count();
                attendanceRate = (double) present / studentAttendances.size();
            }

            // Calculate grades
            List<FinalGradeDTO> finalGrades = gradeService.getSectionFinalGrades(sectionId);
            FinalGradeDTO studentFinalGrade = finalGrades.stream()
                    .filter(fg -> fg.getStudentId().equals(studentId))
                    .findFirst()
                    .orElse(null);
            Double finalAverage = studentFinalGrade != null ? studentFinalGrade.getFinalAverage() : null;

            boolean lowGrades = finalAverage != null && finalAverage < 11.5;
            boolean lowAttendance = attendanceRate < 0.70;

            String reason = "";
            if (lowGrades && lowAttendance) {
                reason = "Bajo rendimiento (Promedio < 11.5) e Inasistencia recurrente (< 70%)";
            } else if (lowGrades) {
                reason = "Bajo rendimiento (Promedio < 11.5)";
            } else if (lowAttendance) {
                reason = "Inasistencia recurrente (< 70%)";
            } else {
                reason = "Asignado manualmente o pendiente de evaluación";
            }

            studentsAtRisk.add(AcademicReportDTO.StudentRiskDTO.builder()
                    .studentId(studentId)
                    .username(enr.getStudent().getUsername())
                    .email(enr.getStudent().getEmail())
                    .sectionCode(enr.getSection().getCode())
                    .courseName(enr.getSection().getCourse().getName())
                    .finalAverage(finalAverage != null ? Math.round(finalAverage * 100.0) / 100.0 : null)
                    .attendanceRate(Math.round(attendanceRate * 10000.0) / 100.0)
                    .riskReason(reason)
                    .build());
        }

        // Critical Courses calculation (courses with highest risk percentage)
        List<AcademicReportDTO.CriticalCourseDTO> criticalCourses = new ArrayList<>();
        Map<Section, List<Enrollment>> enrollmentsBySection = enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::getSection));

        for (Map.Entry<Section, List<Enrollment>> entry : enrollmentsBySection.entrySet()) {
            Section section = entry.getKey();
            List<Enrollment> sectionEnrollments = entry.getValue();

            long totalInSection = sectionEnrollments.size();
            long atRiskInSection = sectionEnrollments.stream()
                    .filter(e -> e.getStatus() == EnrollmentStatus.AT_RISK)
                    .count();

            double riskPercentage = totalInSection > 0 ? (double) atRiskInSection / totalInSection : 0.0;

            criticalCourses.add(AcademicReportDTO.CriticalCourseDTO.builder()
                    .sectionId(section.getId())
                    .sectionCode(section.getCode())
                    .courseName(section.getCourse().getName())
                    .totalStudents(totalInSection)
                    .atRiskCount(atRiskInSection)
                    .riskPercentage(Math.round(riskPercentage * 10000.0) / 100.0)
                    .build());
        }

        // Sort critical courses by risk percentage desc
        criticalCourses.sort(Comparator.comparing(AcademicReportDTO.CriticalCourseDTO::getRiskPercentage).reversed());

        return AcademicReportDTO.builder()
                .totalStudents(totalStudents)
                .activeStudentsCount(activeCount)
                .observedStudentsCount(riskCount)
                .withdrawnStudentsCount(withdrawnCount)
                .overallAttendanceRate(Math.round(overallAttendance * 10000.0) / 100.0)
                .criticalCourses(criticalCourses.stream().limit(5).collect(Collectors.toList()))
                .studentsAtRisk(studentsAtRisk)
                .build();
    }
}
