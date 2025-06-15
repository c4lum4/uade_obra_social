package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.entity.dto.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    boolean loginUser(String username, String password);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteUserById(Long id);

    void save(User user);

    String actualizarFotoPerfil(Long id, org.springframework.web.multipart.MultipartFile file);
}

