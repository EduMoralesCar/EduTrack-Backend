package com.utp.EduTrack.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class GradeDTO {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private Double score;
    private String teacherComment;
}

