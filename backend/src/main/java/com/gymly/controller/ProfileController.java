package com.gymly.controller;

import com.gymly.dto.ApiResponse;
import com.gymly.dto.UpdateProfileRequest;
import com.gymly.dto.UserProfileResponse;
import com.gymly.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ApiResponse<UserProfileResponse> getProfile() {
        return ApiResponse.success(profileService.getProfile());
    }

    @PutMapping
    public ApiResponse<UserProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse response = profileService.updateProfile(request);
        return ApiResponse.success("Profile updated", response);
    }
}
