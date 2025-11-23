package com.chubb.FlightBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.AccessNotGrantedException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.service.FlightService;
import com.chubb.FlightBookingSystem.service.ScheduleService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/airline")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private FlightService flightService;

    private final String adminSecretKey = "Admin";

    @PostMapping("/inventory/add")
    public Mono<ResponseEntity<String>> saveSchedule(
            @RequestHeader(value = "Admin_key", required = false) String adminKey,
            @RequestBody @Valid ScheduleRequestDTO scheduleDto) {

        if (adminKey == null || !adminSecretKey.equals(adminKey)) {
            return Mono.error(new AccessNotGrantedException("Access denied: Invalid or missing Admin Key"));
        }

        return scheduleService.addSchedule(scheduleDto)
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id));
    }

    @PostMapping("/route/add")
    public Mono<ResponseEntity<String>> saveFlight(
            @RequestHeader(value = "Admin_key", required = false) String adminKey,
            @RequestBody @Valid Flight flight) {

        if (adminKey == null || !adminSecretKey.equals(adminKey)) {
            return Mono.error(new AccessNotGrantedException("Access denied: Invalid or missing Admin Key"));
        }

        return flightService.addFlight(flight)
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id));
    }
}
