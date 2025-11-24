package com.chubb.FlightBookingSystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tickets")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    private String id;

    @NotBlank
    private String firstName;

    private String lastName;

    @Min(value = 0)
    private int age;

    private Gender gender;

    private String seatNumber;

    private MealOption mealOption;

    private TicketStatus status = TicketStatus.CONFIRMED;

    private String scheduleId;

    @DBRef
    private Booking booking;

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public enum MealOption {
        VEG,
        NONVEG
    }

    public enum TicketStatus {
        CONFIRMED,
        CANCELLED
    }

    public Ticket(@NotBlank String firstName, String lastName, @Min(0) int age, Gender gender,
                  String seatNumber, MealOption mealOption, String scheduleId, Booking booking) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.seatNumber = seatNumber;
        this.mealOption = mealOption;
        this.booking = booking;
        this.scheduleId = scheduleId;
    }
}
