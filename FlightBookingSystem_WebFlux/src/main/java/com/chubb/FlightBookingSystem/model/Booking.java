package com.chubb.FlightBookingSystem.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    private String pnr;

    private boolean roundTrip;

    private int departureScheduleId;

    private Integer returnScheduleId = null;

    private double totalAmount;

    @Email
    private String emailId;

    @Min(value = 1)
    private int passengerCount;

    public Booking() {
        super();
    }

    public Booking(boolean roundTrip, int departureScheduleId, Integer returnScheduleId,
                   double totalAmount, @Email String emailId, @Min(1) int passengerCount) {
        super();
        this.pnr = RandomStringUtils.randomAlphanumeric(6);
        this.roundTrip = roundTrip;
        this.departureScheduleId = departureScheduleId;
        this.returnScheduleId = returnScheduleId;
        this.totalAmount = totalAmount;
       	this.emailId = emailId;
        this.passengerCount = passengerCount;
    }

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

    public int getDepartureScheduleId() {
        return departureScheduleId;
    }

    public void setDepartureScheduleId(int departureScheduleId) {
        this.departureScheduleId = departureScheduleId;
    }

    public Integer getReturnScheduleId() {
        return returnScheduleId;
    }

    public void setReturnScheduleId(Integer returnScheduleId) {
        this.returnScheduleId = returnScheduleId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }
}
