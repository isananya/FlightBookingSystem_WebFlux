package com.chubb.FlightBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.chubb.FlightBookingSystem.dto.FlightSearchRequestDTO;
import com.chubb.FlightBookingSystem.service.ScheduleService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/flights")
public class FlightSearchController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/search")
    public Mono<ResponseEntity<?>> searchFlights(@RequestBody FlightSearchRequestDTO request) {
        if (request.getSourceAirport() == null ||
            request.getDestinationAirport() == null ||
            request.getDepartureDate() == null) {

            return Mono.just(ResponseEntity.badRequest().build());
        }

        return scheduleService.searchFlights(request).map(results -> ResponseEntity.ok(results));
    }
}
