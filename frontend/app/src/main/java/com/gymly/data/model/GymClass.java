package com.gymly.data.model;

public class GymClass {

    private long id;
    private String name;
    private String description;
    private String trainerName;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String location;
    private int maxCapacity;
    private int spotsRemaining;
    private boolean bookedByUser;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getSpotsRemaining() {
        return spotsRemaining;
    }

    public boolean isBookedByUser() {
        return bookedByUser;
    }
}
