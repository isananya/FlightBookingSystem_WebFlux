package com.chubb.FlightBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.exceptions.UserAlreadyExistsException;
import com.chubb.FlightBookingSystem.model.User;
import com.chubb.FlightBookingSystem.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Mono<User> insertUser(User user) {
        return userRepository.existsByEmailId(user.getEmailId())
    		.flatMap(exists -> {
                if (exists) {
                    return Mono.error(new UserAlreadyExistsException(user.getEmailId()));
                }
                return userRepository.save(user);
            });
    }
}
