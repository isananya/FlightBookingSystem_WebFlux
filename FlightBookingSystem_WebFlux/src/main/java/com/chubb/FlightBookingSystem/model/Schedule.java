package com.chubb.FlightBookingSystem.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;

import jakarta.validation.constraints.NotBlank;

@Document(collection = "schedules")
public class Schedule {

    @Id
    private String id;

    @NotBlank
    private String airlineName;
    
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

    public Schedule() {
    }

    public Schedule(ScheduleRequestDTO dto, Flight flight) {
        this.flight = flight;
        this.airlineName = dto.getAirlineName();
        this.departureDate = dto.getDepartureDate();
        this.basePrice = dto.getBasePrice();
        this.totalSeats = dto.getTotalSeats();
        this.availableSeats = dto.getAvailableSeats();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Set<String> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(Set<String> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(FlightStatus flightStatus) {
        this.flightStatus = flightStatus;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
