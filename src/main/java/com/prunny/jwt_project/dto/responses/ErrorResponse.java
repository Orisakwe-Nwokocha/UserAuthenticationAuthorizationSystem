package com.prunny.jwt_project.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorResponse {
    @JsonFormat(pattern = "dd-MMMM-yyyy 'at' hh:mm a")
    private LocalDateTime requestTime;
    private boolean success;
    private String error;
    private String message;
    private String path;
}
