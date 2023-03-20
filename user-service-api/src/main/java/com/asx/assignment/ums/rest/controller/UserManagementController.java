package com.asx.assignment.ums.rest.controller;

import com.asx.assignment.ums.rest.dto.UserDTO;
import com.asx.assignment.ums.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/userdetails")
public class UserManagementController {
    final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        final UserDTO createdUserDTO = userService.saveOrUpdate(userDTO);
        final URI responseUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdUserDTO.getId()).toUri();
        return ResponseEntity.created(responseUri).body(createdUserDTO);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.accepted().body(userService.saveOrUpdate(userDTO));
    }

    @GetMapping("{id}")
    public @ResponseBody ResponseEntity<UserDTO> findUser(@PathVariable("id") long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
