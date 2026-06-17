package com.utp.EduTrack.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(description = "Resumen de notas finales del estudiante en una sección")
public class FinalGradeDTO {

    @Schema(example = "3")
    private Long studentId;

    @Schema(example = "estudiante1")
    private String studentUsername;

    @Schema(example = "15.0")
    private Double pc1;

    @Schema(example = "14.5")
    private Double pc2;

    @Schema(example = "16.0")
    private Double pc3;

    @Schema(example = "18.0")
    private Double pa; // Promedio de TAREAS

    @Schema(example = "13.0")
    private Double exfinal;

    @Schema(example = "14.8")
    private Double finalAverage; // Calculado
}
