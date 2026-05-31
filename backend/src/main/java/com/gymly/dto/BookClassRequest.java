package com.gymly.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookClassRequest {

    @NotNull(message = "Class ID is required")
    private Long classId;
}
