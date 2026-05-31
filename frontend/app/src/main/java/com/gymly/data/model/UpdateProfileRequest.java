package com.gymly.data.model;

public class UpdateProfileRequest {

    private final String fullName;
    private final String phone;

    public UpdateProfileRequest(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }
}
