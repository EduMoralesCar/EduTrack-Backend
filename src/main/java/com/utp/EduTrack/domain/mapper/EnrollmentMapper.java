package com.utp.EduTrack.domain.mapper;

import com.utp.EduTrack.domain.dto.EnrollmentDTO;
import com.utp.EduTrack.persistance.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public EnrollmentDTO toDto(Enrollment enrollment) {
        return EnrollmentDTO.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentUsername(enrollment.getStudent().getUsername())
                .sectionId(enrollment.getSection().getId())
                .sectionCode(enrollment.getSection().getCode())
                .period(enrollment.getSection().getPeriod())
                .status(enrollment.getStatus())
                .build();
    }
}
