package com.chubb.FlightBookingSystem;

import com.chubb.FlightBookingSystem.dto.BookingRequestDTO;
import com.chubb.FlightBookingSystem.dto.TicketRequestDTO;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.FlightRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingFlowIntegrationTest {
	private WebTestClient webTestClient; 

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        this.webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
    }
	

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private Schedule departureSchedule;

    @BeforeEach
    public void setUp() {
        flightRepository.deleteAll().block();
        scheduleRepository.deleteAll().block();
        bookingRepository.deleteAll().block();
        ticketRepository.deleteAll().block();

        Flight flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setSourceAirport("JFK");
        flight.setDestinationAirport("LAX");
        flightRepository.save(flight).block();

        departureSchedule = new Schedule();
        departureSchedule.setFlight(flight);
        departureSchedule.setDepartureDate(LocalDate.now());
        departureSchedule.setAvailableSeats(50);
        departureSchedule.setBasePrice(100.0f);
        scheduleRepository.save(departureSchedule).block();
    }

    @Test
    public void testBookingFlow() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setRoundTrip(false);
        bookingRequest.setDepartureScheduleId(departureSchedule.getId());
        bookingRequest.setEmailId("test@example.com");
        bookingRequest.setPassengerCount(1);

        TicketRequestDTO passenger = new TicketRequestDTO();
        passenger.setFirstName("John");
        passenger.setLastName("Doe");
        passenger.setDepartureSeatNumber("1A");
        Set<TicketRequestDTO> passengers = new HashSet<>();
        passengers.add(passenger);
        bookingRequest.setPassengers(new HashSet<>(passengers));


        String pnr = webTestClient.post()
                .uri("/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookingRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult().getResponseBody().replace("Booking Successful! PNR : ", "");


        bookingRepository.findByPnr(pnr)
                .doOnNext(booking -> {
                    assertEquals(bookingRequest.getEmailId(), booking.getEmailId());
                    assertEquals(bookingRequest.getPassengerCount(), booking.getPassengerCount());
                })
                .block();


        ticketRepository.findAll()
                .doOnNext(ticket -> {
                    assertEquals(passenger.getFirstName(), ticket.getFirstName());
                    assertEquals(passenger.getLastName(), ticket.getLastName());
                })
                .blockLast();
    }
}
