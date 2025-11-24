package com.chubb.FlightBookingSystem.model;

import java.time.Duration;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chubb.FlightBookingSystem.dto.FlightRequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "flights")
@Data
@NoArgsConstructor
public class Flight {

    @Id
    @NotBlank
    private String flightNumber;

    @NotBlank
    private String sourceAirport;

    @NotBlank
    private String destinationAirport;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private Duration duration;

    public Flight(FlightRequestDTO dto) {
        this.flightNumber = dto.getFlightNumber();
        this.sourceAirport = dto.getSourceAirport();
        this.destinationAirport = dto.getDestinationAirport();
        this.departureTime = dto.getDepartureTime();
        this.arrivalTime = dto.getArrivalTime();
        this.duration = dto.getDuration();
    }
}
