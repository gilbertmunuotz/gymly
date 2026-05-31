package com.gymly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtBookingResponse {

    private Long bookingId;
    private String trainerName;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private String status;
    private String notes;
}
