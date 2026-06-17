package com.utp.EduTrack.domain.service;

import com.utp.EduTrack.domain.dto.AttendanceDTO;
import com.utp.EduTrack.domain.dto.AttendanceJustificationDTO;
import com.utp.EduTrack.domain.exception.BusinessException;
import com.utp.EduTrack.domain.exception.ResourceNotFoundException;
import com.utp.EduTrack.persistance.entity.Attendance;
import com.utp.EduTrack.persistance.entity.AttendanceJustification;
import com.utp.EduTrack.persistance.entity.JustificationStatus;
import com.utp.EduTrack.persistance.entity.Section;
import com.utp.EduTrack.persistance.entity.User;
import com.utp.EduTrack.persistance.repository.AttendanceJustificationRepository;
import com.utp.EduTrack.persistance.repository.AttendanceRepository;
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
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceJustificationRepository justificationRepository;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public AttendanceDTO recordAttendance(AttendanceDTO dto) {
        Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Attendance> attendances = attendanceRepository.findBySectionIdAndStudentIdAndDate(
                dto.getSectionId(), dto.getStudentId(), dto.getDate());
        Attendance attendance;

        if (!attendances.isEmpty()) {
            attendance = attendances.get(0);
            attendance.setStatus(dto.getStatus());
            if (attendances.size() > 1) {
                for (int i = 1; i < attendances.size(); i++) {
                    attendanceRepository.delete(attendances.get(i));
                }
            }
        } else {
            attendance = Attendance.builder()
                    .section(section)
                    .student(student)
                    .date(dto.getDate())
                    .status(dto.getStatus())
                    .build();
        }

        Attendance saved = attendanceRepository.save(attendance);
        dto.setId(saved.getId());
        return dto;
    }

    @Transactional
    public AttendanceJustificationDTO submitJustification(Long attendanceId, String reason, MultipartFile proofFile) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        String proofFilePath = null;
        if (proofFile != null && !proofFile.isEmpty()) {
            proofFilePath = fileStorageService.storeFile(proofFile);
        }

        AttendanceJustification justification = AttendanceJustification.builder()
                .attendance(attendance)
                .reason(reason)
                .proofFilePath(proofFilePath)
                .status(JustificationStatus.PENDIENTE)
                .submittedAt(LocalDateTime.now())
                .build();

        AttendanceJustification saved = justificationRepository.save(justification);

        return AttendanceJustificationDTO.builder()
                .id(saved.getId())
                .attendanceId(saved.getAttendance().getId())
                .reason(saved.getReason())
                .proofFilePath(saved.getProofFilePath())
                .status(saved.getStatus())
                .submittedAt(saved.getSubmittedAt())
                .build();
    }

    @Transactional
    public void resolveJustification(Long justificationId, JustificationStatus status) {
        AttendanceJustification justification = justificationRepository.findById(justificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Justification not found"));
        
        justification.setStatus(status);
        justificationRepository.save(justification);
    }

    @Transactional(readOnly = true)
    public List<AttendanceDTO> getSectionAttendance(Long sectionId) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth != null ? auth.getName() : "";
        boolean isStudent = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));

        return attendanceRepository.findBySectionId(sectionId).stream()
                .filter(a -> !isStudent || a.getStudent().getUsername().equalsIgnoreCase(currentUsername))
                .map(a -> AttendanceDTO.builder()
                        .id(a.getId())
                        .sectionId(a.getSection().getId())
                        .studentId(a.getStudent().getId())
                        .date(a.getDate())
                        .status(a.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
