package com.chubb.FlightBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.FlightBookingSystem.model.User;
import com.chubb.FlightBookingSystem.service.UserService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public Mono<User> saveUser(@RequestBody @Valid User user) {
        return userService.insertUser(user).thenReturn(user);
    }
}
