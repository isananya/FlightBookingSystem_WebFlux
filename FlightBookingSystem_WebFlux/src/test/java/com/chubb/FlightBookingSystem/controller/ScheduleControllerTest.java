package com.chubb.FlightBookingSystem.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.chubb.FlightBookingSystem.dto.FlightRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.AccessNotGrantedException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.service.FlightService;
import com.chubb.FlightBookingSystem.service.ScheduleService;

import reactor.core.publisher.Mono;

public class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //ADD SCHEDULE SUCCESS
    @Test
    void testSaveSchedule_ReturnsCreated() {
        ScheduleRequestDTO dto = new ScheduleRequestDTO();
        when(scheduleService.addSchedule(any(ScheduleRequestDTO.class)))
                .thenReturn(Mono.just("schedule123"));

        ResponseEntity<String> response =
                scheduleController.saveSchedule("Admin", dto).block();

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("schedule123", response.getBody());
    }

    //ADD SCHEDULE - NO ADMIN KEY
    @Test
    void testSaveSchedule_InvalidAdminKey() {
        ScheduleRequestDTO dto = new ScheduleRequestDTO();

        Exception ex = assertThrows(
                AccessNotGrantedException.class,
                () -> scheduleController.saveSchedule(null, dto).block()
        );

        assertTrue(ex.getMessage().contains("Access denied"));
    }

    // ADD FLIGHT SUCCESS
    @Test
    void testSaveFlight_ReturnsCreated() {
        FlightRequestDTO dto = new FlightRequestDTO();

        when(flightService.addFlight(any(Flight.class)))
                .thenReturn(Mono.just("AI101"));

        ResponseEntity<String> response =
                scheduleController.saveFlight("Admin", dto).block();

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("AI101", response.getBody());
    }

    // ADD FLIGHT - NO ADMIN KEY
    @Test
    void testSaveFlight_InvalidAdminKey() {
        FlightRequestDTO dto = new FlightRequestDTO();

        Exception ex = assertThrows(
                AccessNotGrantedException.class,
                () -> scheduleController.saveFlight(null, dto).block()
        );

        assertTrue(ex.getMessage().contains("Access denied"));
    }
}
