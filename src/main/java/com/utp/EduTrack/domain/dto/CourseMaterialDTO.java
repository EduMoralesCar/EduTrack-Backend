package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(description = "Material de estudio subido por el docente")
public class CourseMaterialDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Diapositivas Semana 1")
    private String title;

    @Schema(example = "uploads/diapositivas_sem1.pdf")
    private String filePath;

    @Schema(example = "1")
    private Integer weekNumber;

    @Schema(example = "2026-06-15T08:30:00")
    private String uploadDate;

    @Schema(example = "1")
    private Long sectionId;

    @Schema(example = "true")
    private Boolean visible;
}
