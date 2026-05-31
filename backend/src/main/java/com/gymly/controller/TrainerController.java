package com.gymly.controller;

import com.gymly.dto.ApiResponse;
import com.gymly.dto.BookPtRequest;
import com.gymly.dto.PtBookingResponse;
import com.gymly.dto.TrainerResponse;
import com.gymly.service.PersonalTrainingService;
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
@RequestMapping("/trainers")
public class TrainerController {

    private final PersonalTrainingService personalTrainingService;

    public TrainerController(PersonalTrainingService personalTrainingService) {
        this.personalTrainingService = personalTrainingService;
    }

    @GetMapping
    public ApiResponse<List<TrainerResponse>> getTrainers() {
        return ApiResponse.success(personalTrainingService.getAvailableTrainers());
    }

    @PostMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PtBookingResponse> bookSession(@Valid @RequestBody BookPtRequest request) {
        PtBookingResponse response = personalTrainingService.bookSession(request);
        return ApiResponse.success("Personal training session booked", response);
    }
}
