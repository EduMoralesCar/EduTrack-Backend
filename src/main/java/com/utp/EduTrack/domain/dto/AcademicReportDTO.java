package com.utp.EduTrack.domain.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AcademicReportDTO {

    private long totalStudents;
    private long activeStudentsCount;
    private long observedStudentsCount;
    private long withdrawnStudentsCount;
    private double overallAttendanceRate;
    
    private List<CriticalCourseDTO> criticalCourses;
    private List<StudentRiskDTO> studentsAtRisk;

    @Data
    @Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CriticalCourseDTO {
        private Long sectionId;
        private String sectionCode;
        private String courseName;
        private long totalStudents;
        private long atRiskCount;
        private double riskPercentage;
    }

    @Data
    @Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class StudentRiskDTO {
        private Long studentId;
        private String username;
        private String email;
        private String sectionCode;
        private String courseName;
        private Double finalAverage;
        private Double attendanceRate;
        private String riskReason;
    }
}
