package com.chubb.FlightBookingSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.exceptions.BookingNotFoundException;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TicketService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;
    
    @Autowired
    public TicketService(BookingRepository bookingRepository, TicketRepository ticketRepository,
			ScheduleRepository scheduleRepository) {
		this.bookingRepository = bookingRepository;
		this.ticketRepository = ticketRepository;
		this.scheduleRepository = scheduleRepository;
	}

	private TicketResponseDTO mapToResponse(Ticket t, Schedule schedule) {
        return new TicketResponseDTO(
            t.getFirstName(),
            t.getLastName(),
            t.getAge(),
            t.getGender(),
            t.getSeatNumber(),
            t.getMealOption(),
            t.getStatus(),
            schedule.getDepartureDate(),
            schedule.getFlight().getSourceAirport(),
            schedule.getFlight().getDestinationAirport(),
            schedule.getFlight().getDepartureTime(),
            schedule.getFlight().getArrivalTime()
        );
    }

    public Flux<TicketResponseDTO> getTicketsByPnr(String pnr) {

        return bookingRepository.findByPnr(pnr)
            .switchIfEmpty(Mono.error(new BookingNotFoundException(pnr)))
            .flatMapMany(booking -> 
                ticketRepository.findByBooking_Id(booking.getId())
            )
            .flatMap(ticket ->
                scheduleRepository.findById(ticket.getScheduleId())
                    .map(schedule -> mapToResponse(ticket, schedule))
            );
    }

    public Flux<TicketResponseDTO> getTicketsByEmail(String emailId) {

        return bookingRepository.findByEmailId(emailId)
            .switchIfEmpty(Mono.error(new BookingNotFoundException(emailId)))
            .flatMap(booking ->
                ticketRepository.findByBooking_Id(booking.getId()) 
            )
            .flatMap(ticket ->
                scheduleRepository.findById(ticket.getScheduleId())       
                    .map(schedule -> mapToResponse(ticket, schedule))
            );
    }
}
