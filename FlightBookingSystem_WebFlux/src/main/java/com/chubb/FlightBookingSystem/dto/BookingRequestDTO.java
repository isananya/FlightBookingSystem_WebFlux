package com.chubb.FlightBookingSystem.dto;

import java.util.HashSet;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BookingRequestDTO {

    @Email
    private String emailId;

    private String departureScheduleId;

    private String returnScheduleId;

    private boolean roundTrip;

    @Min(value = 1)
    private int passengerCount;

    private HashSet<TicketRequestDTO> passengers;
}
