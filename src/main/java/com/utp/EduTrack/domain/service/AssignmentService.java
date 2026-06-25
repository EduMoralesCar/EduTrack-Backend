package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.AssignmentDTO;
import com.utp.EduTrack.domain.dto.AssignmentSubmissionDTO;
import com.utp.EduTrack.domain.exception.BusinessException;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import com.utp.EduTrack.persistance.entity.Assignment;
import com.utp.EduTrack.persistance.entity.AssignmentSubmission;
import com.utp.EduTrack.persistance.entity.Section;
import com.utp.EduTrack.persistance.entity.User;
import com.utp.EduTrack.persistance.repository.AssignmentRepository;
import com.utp.EduTrack.persistance.repository.AssignmentSubmissionRepository;
import com.utp.EduTrack.persistance.repository.SectionRepository;
import com.utp.EduTrack.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository submissionRepository;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public AssignmentDTO createAssignment(AssignmentDTO dto, MultipartFile file) {
        Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        String instructionsPath = null;
        if (file != null && !file.isEmpty()) {
            instructionsPath = fileStorageService.storeFile(file);
        }

        Assignment assignment = Assignment.builder()
                .name(dto.getName())
                .type(dto.getType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .weekNumber(dto.getWeekNumber())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .instructionsFilePath(instructionsPath)
                .maxAttempts(dto.getMaxAttempts() != null ? dto.getMaxAttempts() : 1)
                .section(section)
                .build();

        Assignment saved = assignmentRepository.save(assignment);
        dto.setId(saved.getId());
        dto.setDescription(saved.getDescription());
        dto.setInstructionsFilePath(normalizeFilePath(saved.getInstructionsFilePath()));
        dto.setMaxAttempts(saved.getMaxAttempts());
        return dto;
    }

    public List<AssignmentDTO> getAssignmentsBySection(Long sectionId) {
        return assignmentRepository.findBySectionId(sectionId).stream()
                .map(a -> AssignmentDTO.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .type(a.getType())
                        .startDate(a.getStartDate())
                        .endDate(a.getEndDate())
                        .weekNumber(a.getWeekNumber())
                        .category(a.getCategory())
                        .description(a.getDescription())
                        .instructionsFilePath(normalizeFilePath(a.getInstructionsFilePath()))
                        .maxAttempts(a.getMaxAttempts())
                        .sectionId(a.getSection().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public AssignmentSubmissionDTO submitAssignment(Long assignmentId, Long studentId, String comment, MultipartFile file) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (LocalDateTime.now().isAfter(assignment.getEndDate())) {
            throw new BusinessException("El plazo para esta tarea ha expirado.");
        }

        // Validate submission attempts limit
        int maxAttempts = assignment.getMaxAttempts() != null ? assignment.getMaxAttempts() : 1;
        int currentAttempts = submissionRepository.countByAssignmentIdAndStudentId(assignmentId, studentId);
        if (currentAttempts >= maxAttempts) {
            throw new BusinessException("Has alcanzado el límite de intentos permitidos para esta evaluación (" + maxAttempts + ").");
        }

        String filePath = fileStorageService.storeFile(file);

        AssignmentSubmission submission = AssignmentSubmission.builder()
                .assignment(assignment)
                .student(student)
                .submissionDate(LocalDateTime.now())
                .filePath(filePath)
                .studentComment(comment)
                .build();

        AssignmentSubmission saved = submissionRepository.save(submission);

        return AssignmentSubmissionDTO.builder()
                .id(saved.getId())
                .assignmentId(saved.getAssignment().getId())
                .studentId(saved.getStudent().getId())
                .submissionDate(saved.getSubmissionDate())
                .filePath(normalizeFilePath(saved.getFilePath()))
                .studentComment(saved.getStudentComment())
                .build();
    }

    @Transactional
    public AssignmentDTO updateAssignment(Long id, AssignmentDTO dto, MultipartFile file) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        assignment.setName(dto.getName());
        assignment.setType(dto.getType());
        assignment.setStartDate(dto.getStartDate());
        assignment.setEndDate(dto.getEndDate());
        assignment.setWeekNumber(dto.getWeekNumber());
        assignment.setCategory(dto.getCategory());
        assignment.setDescription(dto.getDescription());
        
        if (dto.getMaxAttempts() != null) {
            assignment.setMaxAttempts(dto.getMaxAttempts());
        }

        if (file != null && !file.isEmpty()) {
            String instructionsPath = fileStorageService.storeFile(file);
            assignment.setInstructionsFilePath(instructionsPath);
        }

        Assignment saved = assignmentRepository.save(assignment);
        
        return AssignmentDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .type(saved.getType())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .weekNumber(saved.getWeekNumber())
                .category(saved.getCategory())
                .description(saved.getDescription())
                .instructionsFilePath(normalizeFilePath(saved.getInstructionsFilePath()))
                .maxAttempts(saved.getMaxAttempts())
                .sectionId(saved.getSection().getId())
                .build();
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmissionDTO> getSectionSubmissions(Long sectionId) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth != null ? auth.getName() : "";
        boolean isStudent = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));

        return submissionRepository.findByAssignmentSectionId(sectionId).stream()
                .filter(s -> !isStudent || s.getStudent().getUsername().equalsIgnoreCase(currentUsername))
                .map(s -> AssignmentSubmissionDTO.builder()
                        .id(s.getId())
                        .assignmentId(s.getAssignment().getId())
                        .studentId(s.getStudent().getId())
                        .submissionDate(s.getSubmissionDate())
                        .filePath(normalizeFilePath(s.getFilePath()))
                        .studentComment(s.getStudentComment())
                        .build())
                .collect(Collectors.toList());
    }

    private String normalizeFilePath(String path) {
        if (path == null) return null;
        String normalized = path.replace("\\", "/");
        int uploadsIdx = normalized.lastIndexOf("uploads/");
        if (uploadsIdx != -1) {
            return normalized.substring(uploadsIdx);
        }
        try {
            java.nio.file.Path p = java.nio.file.Paths.get(path);
            return "uploads/" + p.getFileName().toString();
        } catch (Exception e) {
            return path;
        }
    }
}
