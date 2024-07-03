package com.prunny.jwt_project.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class RegisterResponse {
    private String message;
    @JsonProperty(value = "user_id")
    private Long id;
    private String username;
    private Set<String> roles;
    @JsonFormat(pattern = "dd-MMMM-yyyy 'at' hh:mm a")
    private LocalDateTime dateRegistered;

}
