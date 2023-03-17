package com.asx.assignment.ums.rest.controller;

import com.asx.assignment.ums.rest.dto.UserDTO;
import com.asx.assignment.ums.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/user")
public class UserManagementController {
    final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        final UserDTO createdUserDTO = userService.saveOrUpdate(userDTO);
        final URI uri = UriComponentsBuilder.fromPath("/user/{id}").build(createdUserDTO);
        return ResponseEntity.ok().body(createdUserDTO);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.accepted().body(userService.saveOrUpdate(userDTO));
    }

    @GetMapping("{id}")
    public @ResponseBody ResponseEntity<UserDTO> findUser(@PathVariable("id") long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
