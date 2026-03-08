package com.neerad.store.controllers;

import com.neerad.store.dtos.UserDto;
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
    public Iterable<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserDto(user.getId(),user.getName(),user.getEmail()))
    //Transforms each User entity into a UserDto, picking only the id, name, and email fields
                // (keeps sensitive data like passwords out of the response)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto>  getUserById(@PathVariable Long id) {
        //@PathVariable grabs a value directly from the URL path and gives it to your method.
       var user = userRepository.findById(id).orElse(null);
       if(user == null) {
           return ResponseEntity.notFound().build();
       }
       //Converts the found User entity into a UserDto (only exposing safe fields)
       var userDto = new UserDto(user.getId(),user.getName(),user.getEmail());
       return ResponseEntity.ok(userDto);
    }
}