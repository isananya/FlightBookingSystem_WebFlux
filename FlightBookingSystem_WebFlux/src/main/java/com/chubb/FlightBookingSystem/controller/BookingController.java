package com.chubb.FlightBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.FlightBookingSystem.dto.BookingRequestDTO;
import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.service.BookingService;
import com.chubb.FlightBookingSystem.service.TicketService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;
    
    @Autowired
    TicketService ticketService;

    @PostMapping
    public Mono<ResponseEntity<String>> saveBooking(@RequestBody @Valid BookingRequestDTO request) {
        return bookingService.addBooking(request)
        		.map(pnr -> ResponseEntity.status(HttpStatus.CREATED).body("Booking Successful! PNR : " + pnr));
    }
    
    @GetMapping("/history/{email}")
    public Flux<TicketResponseDTO> getTicketsByEmail(@PathVariable String email) {
        return ticketService.getTicketsByEmail(email);
    }
    
    @DeleteMapping("/cancel/{pnr}")
    public Mono<ResponseEntity<Object>> cancelBooking(@PathVariable String pnr) {
        return bookingService.cancelBooking(pnr)
            .then(Mono.just(ResponseEntity.ok().build()))
            .onErrorResume(ex ->
                Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build())
            );
    }
}
