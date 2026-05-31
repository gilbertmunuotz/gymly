package com.gymly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassBookingResponse {

    private Long bookingId;
    private String className;
    private String dayOfWeek;
    private String startTime;
    private String location;
    private LocalDateTime bookedAt;
}
