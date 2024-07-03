package com.prunny.jwt_project.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    @JsonFormat(pattern = "dd-MMMM-yyyy 'at' hh:mm a")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime requestTime;
    private boolean success;
    private T data;
}
