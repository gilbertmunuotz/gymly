package com.gymly.controller;

import com.gymly.dto.ApiResponse;
import com.gymly.dto.MembershipPlanResponse;
import com.gymly.dto.MembershipResponse;
import com.gymly.dto.SubscribeRequest;
import com.gymly.service.MembershipService;
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
@RequestMapping("/memberships")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping("/plans")
    public ApiResponse<List<MembershipPlanResponse>> getPlans() {
        return ApiResponse.success(membershipService.getAllPlans());
    }

    @GetMapping("/active")
    public ApiResponse<MembershipResponse> getActiveMembership() {
        return ApiResponse.success(membershipService.getActiveMembership());
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MembershipResponse> subscribe(@Valid @RequestBody SubscribeRequest request) {
        MembershipResponse response = membershipService.subscribe(request.getPlanId());
        return ApiResponse.success("Subscription successful", response);
    }
}
