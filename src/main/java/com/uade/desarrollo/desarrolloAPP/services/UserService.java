package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.entity.dto.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    boolean loginUser(String username, String password);

    Optional<User> findByUsername(String username);

    List<User> findAll();
}

