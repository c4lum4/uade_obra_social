package com.uade.desarrollo.desarrolloAPP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // Nombre de usuario único

    @JsonIgnore
    @Column(nullable = false)
    private String password; // Contraseña, se debe ignorar en las respuestas JSON

    @Column(nullable = false, unique = true)
    private String email; // Email único

    @Column(nullable = false)
    private String name; // Nombre del usuario

    @Column(nullable = false)
    private String surname; // Apellido del usuario

    @Column
    private String home_address; // Dirección del hogar

    @Column
    private String phone_number; // Número de teléfono


    // Constructor sin el campo de rol (por defecto USER)
    public User(Long id, String username, String email, String password, String name, String surname, String home_address, String phone_number) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.home_address = home_address;
        this.phone_number = phone_number;
    }
}

