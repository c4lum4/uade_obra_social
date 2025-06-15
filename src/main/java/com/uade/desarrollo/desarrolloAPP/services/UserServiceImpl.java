package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    

    @Override
    public User registerUser(User user) {
    
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean loginUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return false; 
        }
        User user = optionalUser.get();
    
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public String actualizarFotoPerfil(Long id, MultipartFile file) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isEmpty()) {
                throw new com.uade.desarrollo.desarrolloAPP.exceptions.NotFoundException("Usuario no encontrado");
            }
            User user = userOpt.get();
            // Carpeta donde se guardarán las fotos (puedes cambiar la ruta si lo deseas)
            String uploadDir = "uploads/fotos-perfil";
            Files.createDirectories(Paths.get(uploadDir));
            // Nombre único para la imagen
            String filename = "user-" + id + "-" + UUID.randomUUID() + "." + getExtension(file.getOriginalFilename());
            Path filePath = Paths.get(uploadDir, filename);
            System.out.println("[DEBUG] Guardando archivo en: " + filePath.toAbsolutePath());
            System.out.println("[DEBUG] Tamaño archivo: " + file.getSize());
            // Guardar archivo
            Files.write(filePath, file.getBytes());
            // Guardar la URL relativa en el usuario
            user.setFotoPerfilUrl("/" + uploadDir + "/" + filename);
            userRepository.save(user);
            return user.getFotoPerfilUrl();
        } catch (Exception e) {
            e.printStackTrace();
            throw new com.uade.desarrollo.desarrolloAPP.exceptions.BusinessException("No se pudo guardar la foto de perfil: " + e.getMessage());
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "jpg";
        int dot = filename.lastIndexOf('.');
        return (dot == -1) ? "jpg" : filename.substring(dot + 1);
    }
}
