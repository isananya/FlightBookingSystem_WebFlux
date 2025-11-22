package com.chubb.FlightBookingSystem.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(String scheduleId) {
        super("Schedule with id " + scheduleId + " doesn't exist");
    }
}