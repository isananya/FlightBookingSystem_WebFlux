package com.chubb.FlightBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.exceptions.FlightAlreadyExistsException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.repository.FlightRepository;

import reactor.core.publisher.Mono;

@Service
public class FlightService {

    private FlightRepository flightRepository;
    
    @Autowired
    public FlightService(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	public Mono<String> addFlight(Flight flight) {
        return flightRepository
        		.existsByFlightNumber(flight.getFlightNumber())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new FlightAlreadyExistsException(flight.getFlightNumber()));
                    }
                    return flightRepository.save(flight).map(saved -> saved.getFlightNumber());
                });
    }
}
