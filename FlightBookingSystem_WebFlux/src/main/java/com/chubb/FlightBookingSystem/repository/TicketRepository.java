package com.chubb.FlightBookingSystem.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Ticket;

import reactor.core.publisher.Flux;

@Repository
public interface TicketRepository extends ReactiveMongoRepository<Ticket, String> {

    Flux<Ticket> findByBooking_Id(String bookingId);
}
