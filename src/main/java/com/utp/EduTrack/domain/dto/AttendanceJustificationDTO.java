package com.utp.EduTrack.domain.dto;

import com.utp.EduTrack.persistance.entity.JustificationStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AttendanceJustificationDTO {
    private Long id;
    private Long attendanceId;
    private String reason;
    private String proofFilePath;
    private JustificationStatus status;
    private LocalDateTime submittedAt;
}

