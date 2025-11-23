package com.chubb.FlightBookingSystem.service;

import com.chubb.FlightBookingSystem.dto.BookingRequestDTO;
import com.chubb.FlightBookingSystem.dto.TicketRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.ScheduleNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.SeatNotAvailableException;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private BookingService bookingService;

    private BookingRequestDTO oneWayBookingRequest;
    private BookingRequestDTO roundTripBookingRequest;
    private Schedule departureSchedule;
    private Schedule returnSchedule;

    @BeforeEach
    void setUp() {
        // One-way booking request
        oneWayBookingRequest = new BookingRequestDTO();
        oneWayBookingRequest.setRoundTrip(false);
        oneWayBookingRequest.setDepartureScheduleId("depSchedule1");
        oneWayBookingRequest.setEmailId("test@example.com");
        oneWayBookingRequest.setPassengerCount(1);

        TicketRequestDTO passenger1 = new TicketRequestDTO();
        passenger1.setFirstName("John");
        passenger1.setLastName("Doe");
        passenger1.setDepartureSeatNumber("10A");
        HashSet<TicketRequestDTO> passengersOneWay = new HashSet<>();
        passengersOneWay.add(passenger1);
        oneWayBookingRequest.setPassengers(passengersOneWay);

        // Round-trip booking request
        roundTripBookingRequest = new BookingRequestDTO();
        roundTripBookingRequest.setRoundTrip(true);
        roundTripBookingRequest.setDepartureScheduleId("depSchedule1");
        roundTripBookingRequest.setReturnScheduleId("retSchedule2");
        roundTripBookingRequest.setEmailId("test@example.com");
        roundTripBookingRequest.setPassengerCount(1);

        TicketRequestDTO passenger2 = new TicketRequestDTO();
        passenger2.setFirstName("Jane");
        passenger2.setLastName("Doe");
        passenger2.setDepartureSeatNumber("11B");
        passenger2.setReturnSeatNumber("12C");
        HashSet<TicketRequestDTO> passengersRoundTrip = new HashSet<>();
        passengersRoundTrip.add(passenger2);
        roundTripBookingRequest.setPassengers(passengersRoundTrip);

        // Schedules
        departureSchedule = new Schedule();
        departureSchedule.setId("depSchedule1");
        departureSchedule.setBasePrice(100.0f);
        departureSchedule.setAvailableSeats(50);
        departureSchedule.setBookedSeats(new HashSet<>());

        returnSchedule = new Schedule();
        returnSchedule.setId("retSchedule2");
        returnSchedule.setBasePrice(150.0f);
        returnSchedule.setAvailableSeats(40);
        returnSchedule.setBookedSeats(new HashSet<>());
    }

    @Test
    public void testAddBooking_OneWay_Success() {
        Booking savedBooking = new Booking();
        savedBooking.setPnr("PNR123");
        
        when(scheduleRepository.findById(anyString())).thenReturn(Mono.empty());

        when(scheduleRepository.existsSeatBooked(anyString(), anyString())).thenReturn(Mono.just(false));
        when(scheduleRepository.findById("depSchedule1")).thenReturn(Mono.just(departureSchedule));
        when(bookingRepository.save(any(Booking.class))).thenReturn(Mono.just(savedBooking));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(Mono.just(new Ticket()));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(Mono.just(departureSchedule));
        

        StepVerifier.create(bookingService.addBooking(oneWayBookingRequest))
                .expectNext("PNR123")
                .verifyComplete();
    }

    @Test
    public void testAddBooking_RoundTrip_Success() {
        Booking savedBooking = new Booking();
        savedBooking.setPnr("PNR456");

        when(scheduleRepository.existsSeatBooked(anyString(), anyString())).thenReturn(Mono.just(false));
        when(scheduleRepository.findById("depSchedule1")).thenReturn(Mono.just(departureSchedule));
        when(scheduleRepository.findById("retSchedule2")).thenReturn(Mono.just(returnSchedule));
        when(bookingRepository.save(any(Booking.class))).thenReturn(Mono.just(savedBooking));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(Mono.just(new Ticket()));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(Mono.just(returnSchedule));

        StepVerifier.create(bookingService.addBooking(roundTripBookingRequest))
                .expectNext("PNR456")
                .verifyComplete();
    }

    @Test
    public void testAddBooking_DepartureSeatNotAvailable() {
    	
        when(scheduleRepository.existsSeatBooked("depSchedule1", "10A")).thenReturn(Mono.just(true));
        when(scheduleRepository.findById(anyString()))
        .thenReturn(Mono.just(departureSchedule));

        StepVerifier.create(bookingService.addBooking(oneWayBookingRequest))
                .expectErrorMatches(throwable -> throwable instanceof SeatNotAvailableException)
                .verify();
    }
}
