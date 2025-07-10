package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User registerUser(User user);
    boolean loginUser(String username, String password);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteUserById(Long id);
    void save(User user);
    String actualizarFotoPerfil(Long id, MultipartFile file);

    void changePassword(Long userId, String currentPassword, String newPassword, String confirmNewPassword);
}
