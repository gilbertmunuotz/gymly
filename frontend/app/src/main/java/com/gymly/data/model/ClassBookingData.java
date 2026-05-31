package com.gymly.data.model;

public class ClassBookingData {

    private long bookingId;
    private String className;
    private String dayOfWeek;
    private String startTime;
    private String location;
    private String bookedAt;

    public long getBookingId() {
        return bookingId;
    }

    public String getClassName() {
        return className;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getLocation() {
        return location;
    }

    public String getBookedAt() {
        return bookedAt;
    }
}
