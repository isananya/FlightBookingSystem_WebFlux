package com.chubb.FlightBookingSystem.model;

import java.time.LocalDate;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chubb.FlightBookingSystem.model.Schedule.FlightStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "bookings")
@Data
@NoArgsConstructor
public class Booking {

    @Id
    private String id;

    private String pnr;

    private boolean roundTrip;

    private String departureScheduleId;

    private String returnScheduleId;

    private double totalAmount;

    @Email
    private String emailId;

    @Min(value = 1)
    private int passengerCount;

    public Booking(boolean roundTrip,
                   String departureScheduleId,
                   String returnScheduleId,
                   double totalAmount,
                   String emailId,
                   int passengerCount) {
        this.pnr = RandomStringUtils.randomAlphanumeric(6);
        this.roundTrip = roundTrip;
        this.departureScheduleId = departureScheduleId;
        this.returnScheduleId = returnScheduleId;
        this.totalAmount = totalAmount;
        this.emailId = emailId;
        this.passengerCount = passengerCount;
    }
}
