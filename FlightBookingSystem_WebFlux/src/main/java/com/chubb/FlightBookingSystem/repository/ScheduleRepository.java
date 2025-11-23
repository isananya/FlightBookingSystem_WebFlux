package com.chubb.FlightBookingSystem.repository;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Schedule;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ScheduleRepository extends ReactiveMongoRepository<Schedule, String> {

	Mono<Boolean> existsByFlight_FlightNumber(String flightNumber);

    Mono<Boolean> existsByDepartureDate(LocalDate departureDate);

    Mono<Schedule> findById(String id);

    Flux<Schedule> findByFlight_FlightNumber(String flightNumber);

    Flux<Schedule> findByDepartureDate(LocalDate date);


    @Query("""
        {
          'flight.sourceAirport': ?0,
          'flight.destinationAirport': ?1,
          'departureDate': { $gte: ?2, $lt: ?3 },
          'availableSeats': { $gte: ?4 },
          'flightStatus': 'SCHEDULED'
        }
        """)
    Flux<Schedule> searchFlights(
            String source,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            int passengerCount
    );
}
