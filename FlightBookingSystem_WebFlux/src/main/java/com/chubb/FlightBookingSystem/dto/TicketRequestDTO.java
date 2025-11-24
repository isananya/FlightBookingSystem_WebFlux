package com.chubb.FlightBookingSystem.dto;

import com.chubb.FlightBookingSystem.model.Ticket.Gender;
import com.chubb.FlightBookingSystem.model.Ticket.MealOption;

import lombok.Data;

@Data
public class TicketRequestDTO {

    private String firstName;
    private String lastName;
    private int age;

    private String departureSeatNumber;
    private String returnSeatNumber;

    private Gender gender;
    private MealOption mealOption;
}
