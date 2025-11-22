package com.chubb.FlightBookingSystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Document(collection = "tickets")
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

    private int scheduleId;

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

    public Ticket() {
        super();
    }

    public Ticket(@NotBlank String firstName, String lastName, @Min(0) int age, Gender gender,
                  String seatNumber, MealOption mealOption, int scheduleId, Booking booking) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public MealOption getMealOption() {
        return mealOption;
    }

    public void setMealOption(MealOption mealOption) {
        this.mealOption = mealOption;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
