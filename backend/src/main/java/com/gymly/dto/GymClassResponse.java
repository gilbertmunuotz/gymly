package com.gymly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GymClassResponse {

    private Long id;
    private String name;
    private String description;
    private String trainerName;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private int maxCapacity;
    private int spotsRemaining;
    private boolean bookedByUser;
}
