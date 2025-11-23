package com.chubb.FlightBookingSystem.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.BookingRequestDTO;
import com.chubb.FlightBookingSystem.dto.TicketRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.BookingNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.ScheduleNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.SeatNotAvailableException;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@Slf4j
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	private Mono<Float> calculateTotalPrice(BookingRequestDTO request) {

		Mono<Float> departureFare = scheduleRepository.findById(request.getDepartureScheduleId())
				.map(Schedule::getBasePrice);

		if (request.isRoundTrip() && request.getReturnScheduleId() != null) {
			Mono<Float> returnFare = scheduleRepository.findById(request.getReturnScheduleId())
					.map(Schedule::getBasePrice);

			return Mono.zip(departureFare, returnFare, (d, r) -> d + r);
		}

		return departureFare;
	}

	public Mono<String> addBooking(BookingRequestDTO request) {

	    HashSet<TicketRequestDTO> passengers = request.getPassengers();

	    Mono<Void> checkDepartureSeats = Flux.fromIterable(passengers)
	            .<Void>concatMap(p -> 
	                scheduleRepository.existsSeatBooked(
	                    request.getDepartureScheduleId(),
	                    p.getDepartureSeatNumber()
	                ).<Void>flatMap(exists -> {
	                    if (exists) {
	                        return Mono.error(new SeatNotAvailableException(
	                            p.getDepartureSeatNumber(),
	                            request.getDepartureScheduleId()
	                        ));
	                    }
	                    return Mono.empty();
	                })
	            ).then();

	    Mono<Void> checkReturnSeats = Mono.empty();

	    if (request.isRoundTrip() && request.getReturnScheduleId() != null) {
	        checkReturnSeats = Flux.fromIterable(passengers)
	            .<Void>concatMap(p -> 
	                scheduleRepository.existsSeatBooked(
	                    request.getReturnScheduleId(),
	                    p.getReturnSeatNumber()
	                ).<Void>flatMap(exists -> {
	                    if (exists) {
	                        return Mono.error(new SeatNotAvailableException(
	                            p.getReturnSeatNumber(),
	                            request.getReturnScheduleId()
	                        ));
	                    }
	                    return Mono.empty();
	                })
	            ).then();
	    }

	    Mono<Schedule> departureScheduleMono = scheduleRepository.findById(request.getDepartureScheduleId())
	            .switchIfEmpty(Mono.error(new ScheduleNotFoundException(request.getDepartureScheduleId())));

	    final Mono<Schedule> returnScheduleMono;
	    if (request.isRoundTrip()) {
	        returnScheduleMono = scheduleRepository.findById(request.getReturnScheduleId())
	                .switchIfEmpty(Mono.error(new ScheduleNotFoundException(request.getReturnScheduleId())));
	    } else {
	        returnScheduleMono = Mono.empty(); 
	    }

	    return checkDepartureSeats
            .then(checkReturnSeats)
            .then(calculateTotalPrice(request))
            .flatMap(totalAmount -> {

                Booking booking = new Booking(
                    request.isRoundTrip(),
                    request.getDepartureScheduleId(),
                    request.getReturnScheduleId(),
                    totalAmount,
                    request.getEmailId(),
                    request.getPassengerCount()
                );

                return bookingRepository.save(booking)
                	    .<Booking>flatMap(savedBooking -> 
                	        
                	        departureScheduleMono.<Booking>flatMap(departureSchedule -> {
                	            
                	            departureSchedule.setAvailableSeats(departureSchedule.getAvailableSeats() - request.getPassengerCount());

                	            return Flux.fromIterable(passengers)
                	                .<Ticket>concatMap(p -> {
                	                    Ticket ticket = new Ticket(
                	                        p.getFirstName(), 
                	                        p.getLastName(), 
                	                        p.getAge(),
                	                        p.getGender(), 
                	                        p.getDepartureSeatNumber(),
                	                        p.getMealOption(), 
                	                        request.getDepartureScheduleId(),
                	                        savedBooking
                	                    );
                	                    
                	                    departureSchedule.getBookedSeats().add(p.getDepartureSeatNumber());

                	                    return ticketRepository.save(ticket);
                	                })
                	                .then(scheduleRepository.save(departureSchedule))
                	                .thenReturn(savedBooking); 
                	        })
                	    )
                	    .<String>flatMap(savedBooking -> {

                	        if (!request.isRoundTrip()) {
                	            return Mono.just(savedBooking.getPnr());
                	        }

                	        return returnScheduleMono.flatMap(returnSchedule -> {
                	            
                	            returnSchedule.setAvailableSeats(returnSchedule.getAvailableSeats() - request.getPassengerCount());

                	            return Flux.fromIterable(passengers)
                	                .<Ticket>concatMap(p -> {
                	                    Ticket ticket = new Ticket(
                	                        p.getFirstName(), 
                	                        p.getLastName(), 
                	                        p.getAge(),
                	                        p.getGender(), 
                	                        p.getReturnSeatNumber(),
                	                        p.getMealOption(), 
                	                        request.getReturnScheduleId(),
                	                        savedBooking
                	                    );

                	                    returnSchedule.getBookedSeats().add(p.getReturnSeatNumber());

                	                    return ticketRepository.save(ticket);
                	                })
                	                .then(scheduleRepository.save(returnSchedule))
                	                .thenReturn(savedBooking.getPnr()); 
                	        });
                	    });
            });
	}
	

	public Mono<Void> cancelBooking(String pnr) {
	    Mono<Booking> bookingMono = bookingRepository.findByPnr(pnr)
	        .switchIfEmpty(Mono.error(() -> new BookingNotFoundException(pnr)));

	    return bookingMono.<Booking>flatMap(booking -> 
	        ticketRepository.findByBooking_Id(booking.getId())
	            .flatMap(ticket -> 
	                Mono.zip(
	                    Mono.just(ticket),
	                    scheduleRepository.findById(ticket.getScheduleId())
	                )
	            )
	            .flatMap(tuple -> {
	                Ticket ticket = tuple.getT1();
	                Schedule schedule = tuple.getT2();

	                if (schedule.getDepartureDate().isBefore(LocalDate.now().plusDays(1))) {
	                    return Mono.<Tuple2<Ticket, Schedule>>error(new RuntimeException(
	                        "Cannot cancel ticket " + ticket.getSeatNumber() +
	                        " for schedule " + schedule.getId() +
	                        ". Cancellation allowed only 24+ hours before departure."
	                    ));
	                }
	                return Mono.just(tuple);
	            })
	            .concatMap(tuple -> {
	                Ticket ticket = tuple.getT1();
	                Schedule schedule = tuple.getT2();
	             
	                ticket.setStatus(Ticket.TicketStatus.CANCELLED);
	                schedule.setAvailableSeats(schedule.getAvailableSeats() + 1);
	                schedule.getBookedSeats().remove(ticket.getSeatNumber());

	                return ticketRepository.save(ticket)
	                    .then(scheduleRepository.save(schedule));
	            })
	            .then(
	                Mono.just(booking)
	                    .doOnNext(b -> b.setTotalAmount(0))
	                    .flatMap(bookingRepository::save)
	            )
	    ).then();
	}
}
