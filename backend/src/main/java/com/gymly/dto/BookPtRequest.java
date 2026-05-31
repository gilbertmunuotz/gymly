package com.gymly.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookPtRequest {

    @NotNull(message = "Trainer ID is required")
    private Long trainerId;

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    private String notes;
}
