package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.EnrollmentCreateDTO;
import com.utp.EduTrack.domain.dto.EnrollmentDTO;
import com.utp.EduTrack.domain.exception.BusinessException;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import com.utp.EduTrack.domain.mapper.EnrollmentMapper;
import com.utp.EduTrack.persistance.entity.Enrollment;
import com.utp.EduTrack.persistance.entity.EnrollmentStatus;
import com.utp.EduTrack.persistance.entity.Role;
import com.utp.EduTrack.persistance.entity.Section;
import com.utp.EduTrack.persistance.entity.User;
import com.utp.EduTrack.persistance.repository.EnrollmentRepository;
import com.utp.EduTrack.persistance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserService userService;
    private final SectionService sectionService;
    private final EnrollmentMapper enrollmentMapper;

    @org.springframework.context.annotation.Lazy
    @org.springframework.beans.factory.annotation.Autowired
    private GradeService gradeService;

    @org.springframework.beans.factory.annotation.Autowired
    private AttendanceRepository attendanceRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private NotificationService notificationService;

    public void evaluateAcademicRisk(Long studentId, Long sectionId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndSectionId(studentId, sectionId)
                .orElse(null);
        if (enrollment == null) {
            return;
        }

        if (enrollment.getStatus() == EnrollmentStatus.WITHDRAWN) {
            return;
        }

        boolean isLowGrades = false;
        boolean isLowAttendance = false;

        // 1. Check Grades
        List<com.utp.EduTrack.domain.dto.FinalGradeDTO> finalGrades = gradeService.getSectionFinalGrades(sectionId);
        com.utp.EduTrack.domain.dto.FinalGradeDTO studentFinalGrade = finalGrades.stream()
                .filter(fg -> fg.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);

        if (studentFinalGrade != null && studentFinalGrade.getFinalAverage() != null) {
            if (studentFinalGrade.getFinalAverage() < 11.5) {
                isLowGrades = true;
            }
        }

        // 2. Check Attendance
        List<com.utp.EduTrack.persistance.entity.Attendance> attendances = attendanceRepository.findBySectionIdAndStudentId(sectionId, studentId);
        if (!attendances.isEmpty()) {
            long totalSessions = attendances.size();
            long presentSessions = attendances.stream()
                    .filter(a -> a.getStatus() == com.utp.EduTrack.persistance.entity.AttendanceStatus.PRESENTE 
                              || a.getStatus() == com.utp.EduTrack.persistance.entity.AttendanceStatus.TARDE
                              || a.getStatus() == com.utp.EduTrack.persistance.entity.AttendanceStatus.JUSTIFICADO)
                    .count();
            double attendanceRate = (double) presentSessions / totalSessions;
            if (attendanceRate < 0.70) {
                isLowAttendance = true;
            }
        }

        EnrollmentStatus oldStatus = enrollment.getStatus();
        EnrollmentStatus newStatus = (isLowGrades || isLowAttendance) ? EnrollmentStatus.AT_RISK : EnrollmentStatus.ENROLLED;

        if (oldStatus != newStatus) {
            enrollment.setStatus(newStatus);
            enrollmentRepository.save(enrollment);

            if (newStatus == EnrollmentStatus.AT_RISK) {
                String message = String.format("Alerta de Riesgo Académico: Has sido marcado con estado 'En Riesgo/Observado' en la sección %s debido a: %s.",
                        enrollment.getSection().getCode(),
                        (isLowGrades && isLowAttendance) ? "bajo rendimiento y faltas recurrentes" :
                                (isLowGrades ? "bajo rendimiento académico" : "inasistencias recurrentes"));
                notificationService.sendNotification(enrollment.getStudent(), message);
            } else if (oldStatus == EnrollmentStatus.AT_RISK && newStatus == EnrollmentStatus.ENROLLED) {
                String message = String.format("Actualización Académica: Tu estado en la sección %s ha vuelto a 'Activo'. ¡Buen trabajo!",
                        enrollment.getSection().getCode());
                notificationService.sendNotification(enrollment.getStudent(), message);
            }
        }
    }

    public EnrollmentDTO updateStatus(Long enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con id: " + enrollmentId));
        
        EnrollmentStatus oldStatus = enrollment.getStatus();
        enrollment.setStatus(status);
        Enrollment saved = enrollmentRepository.save(enrollment);

        if (oldStatus != status) {
            if (status == EnrollmentStatus.AT_RISK) {
                notificationService.sendNotification(saved.getStudent(), 
                        "Actualización de Estado: Tu matrícula en la sección " + saved.getSection().getCode() + " ha sido cambiada a 'En Riesgo/Observado'.");
            } else if (status == EnrollmentStatus.ENROLLED) {
                notificationService.sendNotification(saved.getStudent(), 
                        "Actualización de Estado: Tu matrícula en la sección " + saved.getSection().getCode() + " ahora figura como 'Activo'.");
            } else if (status == EnrollmentStatus.WITHDRAWN) {
                notificationService.sendNotification(saved.getStudent(), 
                        "Actualización de Estado: Has sido registrado como 'Retirado' en la sección " + saved.getSection().getCode() + ".");
            }
        }

        return enrollmentMapper.toDto(saved);
    }



    @Transactional(readOnly = true)
    public List<EnrollmentDTO> findBySection(Long sectionId) {
        sectionService.getSectionEntity(sectionId);
        return enrollmentRepository.findBySectionId(sectionId).stream()
                .map(enrollmentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> findByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(enrollmentMapper::toDto)
                .toList();
    }

    public EnrollmentDTO enroll(EnrollmentCreateDTO request) {
        User student = userService.getUserEntity(request.getStudentId());
        Section section = sectionService.getSectionEntity(request.getSectionId());

        if (student.getRole() != Role.STUDENT) {
            throw new BusinessException("Solo estudiantes pueden matricularse en una sección");
        }
        if (!student.isActive()) {
            throw new BusinessException("El estudiante debe estar activo");
        }
        if (enrollmentRepository.existsByStudentIdAndSectionId(student.getId(), section.getId())) {
            throw new BusinessException("El estudiante ya está matriculado en esta sección");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .section(section)
                .status(EnrollmentStatus.ENROLLED)
                .build();

        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    public void unenroll(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con id: " + enrollmentId));
        enrollmentRepository.delete(enrollment);
    }
}
