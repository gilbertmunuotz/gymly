package com.gymly.controller;

import com.gymly.dto.ApiResponse;
import com.gymly.dto.AttendanceResponse;
import com.gymly.dto.CheckInRequest;
import com.gymly.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/check-in")
public class CheckInController {

    private final AttendanceService attendanceService;

    public CheckInController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/history")
    public ApiResponse<List<AttendanceResponse>> getHistory() {
        return ApiResponse.success(attendanceService.getRecentCheckIns());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AttendanceResponse> checkIn(@Valid @RequestBody(required = false) CheckInRequest request) {
        CheckInRequest body = request != null ? request : new CheckInRequest();
        AttendanceResponse response = attendanceService.checkIn(body);
        return ApiResponse.success("Checked in successfully", response);
    }
}
