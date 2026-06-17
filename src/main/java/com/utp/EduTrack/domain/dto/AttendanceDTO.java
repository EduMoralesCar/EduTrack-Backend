package com.utp.EduTrack.domain.dto;

import com.utp.EduTrack.persistance.entity.AttendanceStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AttendanceDTO {
    private Long id;
    private Long sectionId;
    private Long studentId;
    private LocalDate date;
    private AttendanceStatus status;
}

