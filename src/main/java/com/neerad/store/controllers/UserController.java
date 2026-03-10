package com.neerad.store.controllers;

import com.neerad.store.dtos.UserDto;
import com.neerad.store.mappers.UserMapper;
import com.neerad.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping()
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "",name = "sort") String sortBy
    ) {
        /*
        @RequestParam      -: says how to filter/search them (/users?name=Neerad)
        (required = false) -: it is to make the sort parameter optional , without this if
        we don't add a sort parameter it will throw an error
        defaultValue = ""  -: as the contains(sort) method below doesn't take null value we have to provide default value an empty String
        name = "sort"      -: tells Spring: ignore the variable name. Look for ?sort= in the URL instead
         */

        if(!Set.of("name", "email").contains(sortBy))
            sortBy ="name";
        /*This is a validation/whitelist check for a sort parameter
        This ensures that someone could only sort by with "name" or "email"
        if someone tries to sort with something else such as password it will reset back to "name"
         */

        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
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
//       var userDto = new UserDto(user.getId(),user.getName(),user.getEmail());
        var userDto = userMapper.toDto(user);
       return ResponseEntity.ok(userDto);
    }
}