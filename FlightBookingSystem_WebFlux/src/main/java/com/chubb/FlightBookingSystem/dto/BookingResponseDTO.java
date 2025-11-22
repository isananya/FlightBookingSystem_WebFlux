package com.chubb.FlightBookingSystem.dto;

import java.util.HashSet;

import com.chubb.FlightBookingSystem.model.Ticket;

public class BookingResponseDTO {

    private String id;
    private String pnr;
    private boolean roundTrip;

    private String departureSourceAirport;
    private String departureDestinationAirport;
    private String arrivalSourceAirport;
    private String arrivalDestinationAirport;

    private double totalAmount;
    private HashSet<Ticket> ticketSet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public boolean isRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(boolean roundTrip) {
        this.roundTrip = roundTrip;
    }

    public String getDepartureSourceAirport() {
        return departureSourceAirport;
    }

    public void setDepartureSourceAirport(String departureSourceAirport) {
        this.departureSourceAirport = departureSourceAirport;
    }

    public String getDepartureDestinationAirport() {
        return departureDestinationAirport;
    }

    public void setDepartureDestinationAirport(String departureDestinationAirport) {
        this.departureDestinationAirport = departureDestinationAirport;
    }

    public String getArrivalSourceAirport() {
        return arrivalSourceAirport;
    }

    public void setArrivalSourceAirport(String arrivalSourceAirport) {
        this.arrivalSourceAirport = arrivalSourceAirport;
    }

    public String getArrivalDestinationAirport() {
        return arrivalDestinationAirport;
    }

    public void setArrivalDestinationAirport(String arrivalDestinationAirport) {
        this.arrivalDestinationAirport = arrivalDestinationAirport;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public HashSet<Ticket> getTicketSet() {
        return ticketSet;
    }

    public void setTicketSet(HashSet<Ticket> ticketSet) {
        this.ticketSet = ticketSet;
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }
}
