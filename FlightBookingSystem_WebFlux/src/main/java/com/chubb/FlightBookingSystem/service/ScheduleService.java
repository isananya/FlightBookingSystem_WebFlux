package com.chubb.FlightBookingSystem.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.FlightSearchRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleResponseDTO;
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
    
    private ScheduleResponseDTO mapToResponseDTO(Schedule schedule) {
        Flight flight = schedule.getFlight();

        ScheduleResponseDTO dto = new ScheduleResponseDTO();
        dto.setId(schedule.getId());
        dto.setDepartureDate(schedule.getDepartureDate());
        dto.setBasePrice(schedule.getBasePrice());
        dto.setAirlineName(schedule.getAirlineName());

        dto.setFlightNumber(flight.getFlightNumber());
        dto.setSourceAirport(flight.getSourceAirport());
        dto.setDestinationAirport(flight.getDestinationAirport());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setDuration(flight.getDuration());

        return dto;
    }

    public Mono<Map<String, Object>> searchFlights(FlightSearchRequestDTO request) {
    	LocalDate scheduleDate = request.getDepartureDate();
        LocalDate scheduleNext = scheduleDate.plusDays(1);
        Mono<List<ScheduleResponseDTO>> departureMono = scheduleRepository.searchFlights(
    		request.getSourceAirport(),
            request.getDestinationAirport(),
            scheduleDate,
            scheduleNext,
            request.getPassengerCount()
        ).map(this::mapToResponseDTO).collectList();

        Boolean roundTrip = request.getRoundTrip();
        Mono<List<ScheduleResponseDTO>> returnMono;
        
        LocalDate returnDate = request.getDepartureDate();
        LocalDate returnNext = returnDate.plusDays(1);
        if (Boolean.TRUE.equals(roundTrip) && request.getReturnDate() != null) {
            returnMono = scheduleRepository.searchFlights( 
        		request.getSourceAirport(),
                request.getDestinationAirport(),
                returnDate,
                returnNext,
                request.getPassengerCount()
            ).map(this::mapToResponseDTO).collectList();
        } 
        
        else {
            returnMono = Mono.just(Collections.emptyList());
        }

        return Mono.zip(departureMono, returnMono).map(tuple -> {
            List<ScheduleResponseDTO> departures = tuple.getT1();
            List<ScheduleResponseDTO> returns = tuple.getT2();

            Map<String, Object> results = new HashMap<>();
            results.put("departure", departures);
            results.put("departureCount", departures.size());
            results.put("return", returns);
            results.put("returnCount", returns.size());

            return results;
        });
    }
}

