package com.chubb.FlightBookingSystem.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "schedules")
@Data
@NoArgsConstructor
public class Schedule {

    @Id
    private String id;

    @NotBlank
    private String airlineName;
    
    @FutureOrPresent
    private LocalDate departureDate;

    private float basePrice;

    private int totalSeats;

    private int availableSeats;

    private Set<String> bookedSeats = new HashSet<>();

    private FlightStatus flightStatus = FlightStatus.SCHEDULED;

    @Field("flight")
    private Flight flight;

    public enum FlightStatus {
        SCHEDULED,
        DELAYED,
        CANCELLED,
        COMPLETED
    }
    
    public Schedule(ScheduleRequestDTO dto, Flight flight) {
        this.flight = flight;
        this.airlineName = dto.getAirlineName();
        this.departureDate = dto.getDepartureDate();
        this.basePrice = dto.getBasePrice();
        this.totalSeats = dto.getTotalSeats();
        this.availableSeats = dto.getAvailableSeats();
    }
}
