/* 
package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public String register(@RequestBody User user) {
        // Aquí puedes agregar validación de datos y verificación si el usuario ya existe

        userRepository.save(user); // Guardar el usuario en la base de datos
        return "Usuario registrado correctamente";
    }
}
*/
