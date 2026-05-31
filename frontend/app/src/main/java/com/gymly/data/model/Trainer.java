package com.gymly.data.model;

public class Trainer {

    private long id;
    private String fullName;
    private String specialty;
    private String bio;
    private boolean available;

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getBio() {
        return bio;
    }

    public boolean isAvailable() {
        return available;
    }
}
