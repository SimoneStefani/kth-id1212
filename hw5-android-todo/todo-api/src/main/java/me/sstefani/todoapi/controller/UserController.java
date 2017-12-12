package me.sstefani.todoapi.controller;

import me.sstefani.todoapi.integration.UserRepository;
import me.sstefani.todoapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }
}
