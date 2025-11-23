package com.chubb.FlightBookingSystem.service;

import com.chubb.FlightBookingSystem.dto.FlightSearchRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleResponseDTO;
import com.chubb.FlightBookingSystem.exceptions.FlightNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.ScheduleAlreadyExistsException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.repository.FlightRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    public void testAddSchedule_Success() {
        ScheduleRequestDTO scheduleRequest = new ScheduleRequestDTO();
        scheduleRequest.setFlightNumber("FL123");
        scheduleRequest.setDepartureDate(LocalDate.now());

        Flight flight = new Flight();
        flight.setFlightNumber("FL123");

        Schedule schedule = new Schedule(scheduleRequest, flight);
        schedule.setId("schedule123");

        when(flightRepository.findById(anyString())).thenReturn(Mono.just(flight));
        when(scheduleRepository.existsByDepartureDate(any(LocalDate.class))).thenReturn(Mono.just(false));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(Mono.just(schedule));

        StepVerifier.create(scheduleService.addSchedule(scheduleRequest))
                .expectNext("schedule123")
                .verifyComplete();
    }

    @Test
    public void testAddSchedule_ScheduleAlreadyExists() {
        ScheduleRequestDTO scheduleRequest = new ScheduleRequestDTO();
        scheduleRequest.setFlightNumber("FL123");
        scheduleRequest.setDepartureDate(LocalDate.now());

        Flight flight = new Flight();
        flight.setFlightNumber("FL123");

        when(flightRepository.findById(anyString())).thenReturn(Mono.just(flight));
        when(scheduleRepository.existsByDepartureDate(any(LocalDate.class))).thenReturn(Mono.just(true));

        StepVerifier.create(scheduleService.addSchedule(scheduleRequest))
                .expectErrorMatches(throwable -> throwable instanceof ScheduleAlreadyExistsException)
                .verify();
    }

    @Test
    public void testSearchFlights_RoundTrip() {
        FlightSearchRequestDTO request = new FlightSearchRequestDTO();
        request.setSourceAirport("DEL");
        request.setDestinationAirport("BOM");
        request.setDepartureDate(LocalDate.now());
        request.setReturnDate(LocalDate.now().plusDays(5));
        request.setRoundTrip(true);
        request.setPassengerCount(1);

        Flight flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setSourceAirport("BOM");
        flight.setDestinationAirport("DEL");
        flight.setDepartureTime(LocalTime.of(10, 0));
        flight.setArrivalTime(LocalTime.of(12, 0));
        flight.setDuration(Duration.ofHours(2).plusMinutes(30));

        Schedule schedule1 = new Schedule();
        schedule1.setId("schedule1");
        schedule1.setDepartureDate(LocalDate.now());
        schedule1.setBasePrice(100.0f);
        schedule1.setAirlineName("Indigo");
        schedule1.setFlight(flight);

        Schedule schedule2 = new Schedule();
        schedule2.setId("schedule2");
        schedule2.setDepartureDate(LocalDate.now().plusDays(5));
        schedule2.setBasePrice(1200.0f);
        schedule2.setAirlineName("Indigo");
        schedule2.setFlight(flight);

        when(scheduleRepository.searchFlights(anyString(), anyString(), any(LocalDate.class), any(LocalDate.class), anyInt()))
                .thenReturn(Flux.just(schedule1, schedule2));

        StepVerifier.create(scheduleService.searchFlights(request))
                .expectNextMatches(results -> {
                    List<ScheduleResponseDTO> departures = (List<ScheduleResponseDTO>) results.get("departure");
                    Integer departureCount = (Integer) results.get("departureCount");
                    List<ScheduleResponseDTO> returns = (List<ScheduleResponseDTO>) results.get("return");
                    Integer returnCount = (Integer) results.get("returnCount");

                    return departures.size() == 2 &&
                            departureCount == 2 &&
                            returns.size() == 2 &&
                            returnCount == 2;
                })
                .verifyComplete();
    }
}
