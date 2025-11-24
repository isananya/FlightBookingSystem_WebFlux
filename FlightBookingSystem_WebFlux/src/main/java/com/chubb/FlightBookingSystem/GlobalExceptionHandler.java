package com.chubb.FlightBookingSystem;

import com.chubb.FlightBookingSystem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler({
        FlightNotFoundException.class,
        ScheduleNotFoundException.class,
        BookingNotFoundException.class
    })
    public ResponseEntity<String> handleNotFoundExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({
        FlightAlreadyExistsException.class,
        ScheduleAlreadyExistsException.class,
        UserAlreadyExistsException.class
    })
    public ResponseEntity<String> handleConflictExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(AccessNotGrantedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessNotGrantedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(SeatNotAvailableException.class)
    public ResponseEntity<String> handleBadRequest(SeatNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + ex.getMessage());
    }
}