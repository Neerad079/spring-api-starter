package com.neerad.store.controllers;

import com.neerad.store.entities.User;
import com.neerad.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping()
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User>  getUserById(@PathVariable Long id) {
        //@PathVariable grabs a value directly from the URL path and gives it to your method.
       var user = userRepository.findById(id).orElse(null);
       if(user == null) {
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(user);
    }
}