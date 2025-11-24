package com.chubb.FlightBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.FlightBookingSystem.dto.UserRequestDTO;
import com.chubb.FlightBookingSystem.model.User;
import com.chubb.FlightBookingSystem.service.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class AuthController{
    private final UserService userService;
    
    @Autowired
    public AuthController(UserService userService) {
    	this.userService=userService;
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> saveUser(@RequestBody @Valid UserRequestDTO dto) {

    	User user = new User(dto);

        return userService.insertUser(user).map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved.getId()));
    }
}
