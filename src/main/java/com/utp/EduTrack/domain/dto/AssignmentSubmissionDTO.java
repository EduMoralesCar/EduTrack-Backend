package com.utp.EduTrack.domain.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AssignmentSubmissionDTO {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private LocalDateTime submissionDate;
    private String filePath;
    private String studentComment;
}

