package com.chubb.FlightBookingSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.chubb.FlightBookingSystem.dto.UserRequestDTO;
import com.chubb.FlightBookingSystem.model.User;
import com.chubb.FlightBookingSystem.service.UserService;

import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup_ReturnsCreatedUserId() {
        // Arrange
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("John Doe");
        dto.setEmailId("john@example.com");
        dto.setMobileNo("9999999999");
        dto.setGender("MALE");
        dto.setAge(25);
        dto.setPassword("pass123");

        User savedUser = new User(dto);
        savedUser.setId("generatedUserId123");

        when(userService.insertUser(any(User.class))).thenReturn(Mono.just(savedUser));

        // Act
        ResponseEntity<String> response = authController.saveUser(dto).block();

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("generatedUserId123", response.getBody());
    }
}
