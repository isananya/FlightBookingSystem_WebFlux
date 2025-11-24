package com.chubb.FlightBookingSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String emailId;

    @NotBlank
    private String mobileNo;

    @NotBlank
    private String gender;

    @Min(0)
    private int age;

    @NotBlank
    private String password;
}
