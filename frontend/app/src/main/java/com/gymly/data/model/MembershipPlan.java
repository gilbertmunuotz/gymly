package com.gymly.data.model;

public class MembershipPlan {

    private long id;
    private String name;
    private String description;
    private double price;
    private int durationDays;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getDurationDays() {
        return durationDays;
    }
}
