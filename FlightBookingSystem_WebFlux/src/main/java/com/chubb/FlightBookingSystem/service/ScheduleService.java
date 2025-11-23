package com.chubb.FlightBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.FlightNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.ScheduleAlreadyExistsException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.repository.FlightRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;

import reactor.core.publisher.Mono;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FlightRepository flightRepository;

    public Mono<String> addSchedule(ScheduleRequestDTO dto) {

        String flightNumber = dto.getFlightNumber();

        return flightRepository.findById(flightNumber)
                .switchIfEmpty(Mono.error(new FlightNotFoundException(flightNumber)))
                .flatMap(flight -> {
                	return scheduleRepository.existsByDepartureDate(dto.getDepartureDate()).flatMap(exists -> {
                		if (exists) {
            				return Mono.error(new ScheduleAlreadyExistsException(flight,dto.getDepartureDate()));
                        }
                			
            			Schedule schedule = new Schedule(dto, flight);

                        return scheduleRepository.save(schedule).map(saved -> saved.getId());
                	});
                });
    }
}
