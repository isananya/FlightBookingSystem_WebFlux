package com.chubb.FlightBookingSystem.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ScheduleRequestDTO {

    private String airlineName;
    private LocalDate departureDate;
    private float basePrice;
    private int totalSeats;
    private int availableSeats;

    private String flightNumber;

}
