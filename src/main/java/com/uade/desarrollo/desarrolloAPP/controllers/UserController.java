package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.entity.dto.UserRequest;
import com.uade.desarrollo.desarrolloAPP.exceptions.UserDuplicateException;
import com.uade.desarrollo.desarrolloAPP.exceptions.UserWrongPasswordException;
import com.uade.desarrollo.desarrolloAPP.repository.PasswordResetTokenRepository;
import com.uade.desarrollo.desarrolloAPP.services.UserService;
import jakarta.validation.Valid;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        // Obtener usuario autenticado desde el contexto de seguridad
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("[DEBUG] Username autenticado: '" + username + "'");
        Optional<User> authUserOpt = userService.findByUsername(username);
        if (authUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado"));
        }
        User authUser = authUserOpt.get();
        System.out.println("[DEBUG] ID usuario autenticado: " + authUser.getId() + ", ID a borrar: " + id);
        if (!authUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("error", "No tenés permiso para eliminar otra cuenta"));
        }
        Optional<User> user = userService.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Usuario no encontrado"));
        }
        // Eliminar tokens de reseteo de contraseña asociados
        passwordResetTokenRepository.findAll().stream()
            .filter(token -> token.getUser().getId().equals(id))
            .forEach(token -> passwordResetTokenRepository.delete(token));
        userService.deleteUserById(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existingUserOptional = userService.findById(id);
        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        User existingUser = existingUserOptional.get();
        existingUser.setName(updatedUser.getName());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setHome_address(updatedUser.getHome_address());
        existingUser.setPhone_number(updatedUser.getPhone_number());

        userService.save(existingUser);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUserField(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<User> existingUserOptional = userService.findById(id);
        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        User existingUser = existingUserOptional.get();

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    existingUser.setName((String) value);
                    break;
                case "surname":
                    existingUser.setSurname((String) value);
                    break;
                case "email":
                    existingUser.setEmail((String) value);
                    break;
                case "home_address":
                    existingUser.setHome_address((String) value);
                    break;
                case "phone_number":
                    existingUser.setPhone_number((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Campo no válido: " + key);
            }
        });

        userService.save(existingUser);
        return ResponseEntity.ok("Campo(s) actualizado(s) correctamente");
    }
}
