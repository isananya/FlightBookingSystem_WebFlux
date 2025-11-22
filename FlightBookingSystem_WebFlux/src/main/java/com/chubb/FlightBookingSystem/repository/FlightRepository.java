package com.chubb.FlightBookingSystem.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Flight;

import reactor.core.publisher.Mono;

@Repository
public interface FlightRepository extends ReactiveMongoRepository<Flight, String> {

    Mono<Boolean> existsByFlightNumber(String flightNumber);
}
