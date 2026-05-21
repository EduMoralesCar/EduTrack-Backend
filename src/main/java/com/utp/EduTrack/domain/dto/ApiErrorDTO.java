package com.utp.EduTrack.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApiErrorDTO {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
}
