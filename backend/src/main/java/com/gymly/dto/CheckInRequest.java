package com.gymly.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CheckInRequest {

    @Pattern(regexp = "GYM|CLASS", message = "checkInType must be GYM or CLASS")
    private String checkInType = "GYM";
}
