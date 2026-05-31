package com.gymly.data.model;

public class RegisterRequest {

    private final String fullName;
    private final String email;
    private final String password;
    private final String phone;

    public RegisterRequest(String fullName, String email, String password, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
