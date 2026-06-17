package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(description = "Curso académico")
public class CourseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Programación I")
    private String name;

    @Schema(example = "PROG101")
    private String code;

    @Schema(example = "Fundamentos de programación")
    private String description;

    @Schema(example = "4")
    private Integer credits;
}

