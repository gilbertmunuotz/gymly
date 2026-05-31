package com.gymly.data.model;

public class BookPtRequest {

    private final long trainerId;
    private final String sessionDate;
    private final String startTime;
    private final String notes;

    public BookPtRequest(long trainerId, String sessionDate, String startTime, String notes) {
        this.trainerId = trainerId;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.notes = notes;
    }
}
