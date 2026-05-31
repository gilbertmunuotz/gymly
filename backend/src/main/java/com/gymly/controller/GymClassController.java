package com.gymly.controller;

import com.gymly.dto.ApiResponse;
import com.gymly.dto.BookClassRequest;
import com.gymly.dto.ClassBookingResponse;
import com.gymly.dto.GymClassResponse;
import com.gymly.service.GymClassService;
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
@RequestMapping("/classes")
public class GymClassController {

    private final GymClassService gymClassService;

    public GymClassController(GymClassService gymClassService) {
        this.gymClassService = gymClassService;
    }

    @GetMapping
    public ApiResponse<List<GymClassResponse>> getAllClasses() {
        return ApiResponse.success(gymClassService.getAllClasses());
    }

    @PostMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ClassBookingResponse> bookClass(@Valid @RequestBody BookClassRequest request) {
        ClassBookingResponse response = gymClassService.bookClass(request);
        return ApiResponse.success("Class booked successfully", response);
    }
}
