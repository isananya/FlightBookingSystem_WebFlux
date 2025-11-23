package com.chubb.FlightBookingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.service.TicketService;

import reactor.core.publisher.Mono;

@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/ticket/{pnr}")
    public Mono<ResponseEntity<List<TicketResponseDTO>>> getTicketsByPnr(@PathVariable String pnr) {
        return ticketService.getTicketsByPnr(pnr).map(ResponseEntity::ok);
    }
}
