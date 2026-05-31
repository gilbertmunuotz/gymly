package com.gymly.service;

import com.gymly.dto.UpdateProfileRequest;
import com.gymly.dto.UserProfileResponse;
import com.gymly.exception.ResourceNotFoundException;
import com.gymly.model.User;
import com.gymly.repository.UserRepository;
import com.gymly.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile() {
        User user = getCurrentUser();
        return toResponse(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();
        user.setFullName(request.getFullName().trim());
        user.setPhone(request.getPhone() != null && !request.getPhone().isBlank()
                ? request.getPhone().trim()
                : null);
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    private User getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserProfileResponse toResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .build();
    }
}
