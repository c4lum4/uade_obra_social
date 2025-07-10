package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.entity.dto.ChangePasswordDTO;
import com.uade.desarrollo.desarrolloAPP.entity.dto.UserProfileDTO;
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

    @Autowired
    private com.uade.desarrollo.desarrolloAPP.repository.TurnoRepository turnoRepository;

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
    }    @GetMapping("/get-user-by-username")
    public ResponseEntity<?> getUser(@RequestParam String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Usuario no encontrado"));
        }
        
        User user = userOpt.get();
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setHome_address(user.getHome_address());
        dto.setPhone_number(user.getPhone_number());
        dto.setFotoPerfilUrl(user.getFotoPerfilUrl()); // Asegura que se incluya la URL de la foto
        
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        System.out.println("[DEBUG] Entró al método deleteUser con id: " + id);
        // Obtener usuario autenticado desde el contexto de seguridad
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("[DEBUG] Username autenticado: " + username);
        Optional<User> authUserOpt = userService.findByUsername(username);
        if (authUserOpt.isEmpty()) {
            System.out.println("[DEBUG] Usuario autenticado no encontrado en base de datos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado"));
        }
        User authUser = authUserOpt.get();
        System.out.println("[DEBUG] ID usuario autenticado: " + authUser.getId() + ", ID a borrar: " + id);
        // Validar que el usuario autenticado sea el mismo que el que quiere borrar
        if (!authUser.getId().equals(id)) {
            System.out.println("[DEBUG] El usuario autenticado no coincide con el ID a borrar");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "No tenés permiso para eliminar otra cuenta. Solo puedes eliminar tu propia cuenta."));
        }
        Optional<User> user = userService.findById(id);
        if (user.isEmpty()) {
            System.out.println("[DEBUG] Usuario a borrar no encontrado en base de datos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }
        // Liberar turnos reservados por el usuario
        var turnosReservados = turnoRepository.findByUsuarioIdAndEstado(id, com.uade.desarrollo.desarrolloAPP.entity.Turno.EstadoTurno.RESERVADO);
        for (var turno : turnosReservados) {
            turno.setEstado(com.uade.desarrollo.desarrolloAPP.entity.Turno.EstadoTurno.DISPONIBLE);
            turno.setUsuario(null);
        }
        turnoRepository.saveAll(turnosReservados);
        // Eliminar tokens de reseteo de contraseña asociados
        passwordResetTokenRepository.findAll().stream()
                .filter(token -> token.getUser().getId().equals(id))
                .forEach(token -> passwordResetTokenRepository.delete(token));
        userService.deleteUserById(id);
        System.out.println("[DEBUG] Usuario eliminado correctamente");
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

    @PutMapping("/{id}/foto-perfil")
    public ResponseEntity<?> actualizarFotoPerfil(@PathVariable Long id, @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "No se recibió ningún archivo"));
            }
            System.out.println("[DEBUG] Nombre archivo recibido: " + file.getOriginalFilename());
            String url = userService.actualizarFotoPerfil(id, file);
            return ResponseEntity.ok(Map.of("mensaje", "Foto de perfil actualizada", "fotoPerfilUrl", url));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error al actualizar la foto de perfil: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/foto-perfil-url")
    public ResponseEntity<?> actualizarFotoPerfilUrl(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String url = body.get("fotoPerfilUrl");
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "La URL de la foto no puede estar vacía"));
        }
        // Validar que la URL apunte a Firebase Storage o Cloudinary
        if (!url.contains("firebasestorage.googleapis.com") && !url.contains("res.cloudinary.com")) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "La URL debe ser de Firebase Storage o Cloudinary"));
        }
        var userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Usuario no encontrado"));
        }
        var user = userOpt.get();
        user.setFotoPerfilUrl(url);
        userService.save(user);
        return ResponseEntity.ok(Map.of("mensaje", "Foto de perfil actualizada", "fotoPerfilUrl", url));
    }
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO dto) {
        String usernameAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        User authUser = userService.findByUsername(usernameAuth)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        if (!authUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para cambiar la contraseña de este usuario");
        }

        try {
            userService.changePassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmNewPassword());
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

