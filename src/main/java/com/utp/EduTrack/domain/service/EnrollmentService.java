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
