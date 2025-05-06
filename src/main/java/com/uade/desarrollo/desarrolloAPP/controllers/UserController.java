package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.entity.dto.UserRequest;
import com.uade.desarrollo.desarrolloAPP.exceptions.UserDuplicateException;
import com.uade.desarrollo.desarrolloAPP.exceptions.UserWrongPasswordException;
import com.uade.desarrollo.desarrolloAPP.services.UserService;
import jakarta.validation.Valid;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRequest userRequest) throws UserDuplicateException {
        if (userService.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new UserDuplicateException();
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setPhone_number(userRequest.getPhone_number());
        user.setHome_address(userRequest.getHome_address());

        User result = userService.registerUser(user);
        return ResponseEntity.created(URI.create("/users/" + result.getId())).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserRequest userRequest)
            throws UserWrongPasswordException, UserDuplicateException {

        Optional<User> existing = userService.findByUsername(userRequest.getUsername());

        if (existing.isEmpty()) {
            throw new UserDuplicateException(); // Usuario no existe
        }

        if (userService.loginUser(userRequest.getUsername(), userRequest.getPassword())) {
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            throw new UserWrongPasswordException();
        }
    }

    @GetMapping("/get-user-by-username")
    public ResponseEntity<User> getUser(@RequestParam String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
}
