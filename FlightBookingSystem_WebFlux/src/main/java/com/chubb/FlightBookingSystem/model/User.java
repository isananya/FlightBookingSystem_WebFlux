package com.chubb.FlightBookingSystem.model;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chubb.FlightBookingSystem.dto.UserRequestDTO;
import com.chubb.FlightBookingSystem.model.Schedule.FlightStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String emailId;

    @NotBlank
    private String mobileNo;

    @NotBlank
    private String gender;

    @Min(value = 0)
    private int age;

    @NotBlank
    private String password;
    
    public User(UserRequestDTO dto) {
        this.name = dto.getName();
        this.emailId = dto.getEmailId();
        this.mobileNo = dto.getMobileNo();
        this.gender = dto.getGender();
        this.age = dto.getAge();
        this.password = dto.getPassword();
    }
}
