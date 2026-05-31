package com.gymly.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscribeRequest {

    @NotNull(message = "Plan ID is required")
    private Long planId;
}
