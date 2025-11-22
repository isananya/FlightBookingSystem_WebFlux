package com.chubb.FlightBookingSystem.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Booking;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BookingRepository extends ReactiveMongoRepository<Booking, String> {

    Mono<Booking> findByPnr(String pnr);

    Flux<Booking> findByEmailId(String emailId);
}
