package com.neerad.store.controllers;

import com.neerad.store.dtos.ChangePasswordRequest;
import com.neerad.store.dtos.RegisterUserRequest;
import com.neerad.store.dtos.UpdateUserRequest;
import com.neerad.store.dtos.UserDto;
import com.neerad.store.mappers.UserMapper;
import com.neerad.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    @PostMapping()
    public ResponseEntity<UserDto> createUser(
            @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder) {
    //@RequestBody tells Spring:Take the raw JSON from the HTTP request body and convert it into a UserDto Java object
        // ResponseEntity : status 201 i.e. successfully created
        var user = userMapper.toEntity(request);
        userRepository.save(user);
        var userDto = userMapper.toDto(user);
        var uri=uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ){
       var user = userRepository.findById(id).orElse(null);
       if(user == null) {
           return ResponseEntity.notFound().build();
       }
       userMapper.updateEntity(request,user);
       userRepository.save(user);
       return ResponseEntity.ok(userMapper.toDto(user));
       //.ok().build():- 200 OK : Request succeeded and here's some data back
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var user =  userRepository.findById(id).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
        // .noContent():- HTTP status = 204 NO content : Request succeeded but there's nothing to return
    }
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ){
        var user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        if(!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            // checks if the old password entered by the user is correct or not , if not then it doesn't allow to change it
            // HTTP response : 401 Unauthorized
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
        }
    }
