package com.chubb.FlightBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chubb.FlightBookingSystem.dto.FlightRequestDTO;
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

    private final ScheduleService scheduleService;
    private final FlightService flightService;

    private final String adminSecretKey;
    
    @Autowired
    public ScheduleController(ScheduleService scheduleService, FlightService flightService) {
		this.scheduleService = scheduleService;
		this.flightService = flightService;
		this.adminSecretKey = "Admin";
	}

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
            @RequestBody @Valid FlightRequestDTO dto) {

        if (adminKey == null || !adminSecretKey.equals(adminKey)) {
            return Mono.error(new AccessNotGrantedException("Access denied: Invalid or missing Admin Key"));
        }
        
        Flight flight = new Flight(dto);

        return flightService.addFlight(flight)
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id));
    }
}
