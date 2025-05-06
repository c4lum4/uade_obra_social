package com.uade.desarrollo.desarrolloAPP.repository;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username); // Método para buscar un usuario por nombre de usuario
}

