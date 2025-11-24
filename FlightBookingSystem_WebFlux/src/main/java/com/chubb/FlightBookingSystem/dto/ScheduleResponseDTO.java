package com.chubb.FlightBookingSystem.dto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ScheduleResponseDTO {

    private String id; 
    private String airlineName;
    private LocalDate departureDate;
    private float basePrice;
    private String flightNumber;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String sourceAirport;
    private String destinationAirport;
    private Duration duration;
}
