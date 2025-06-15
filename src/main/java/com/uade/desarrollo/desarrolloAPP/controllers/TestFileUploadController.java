package com.uade.desarrollo.desarrolloAPP.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/test-upload")
public class TestFileUploadController {
    @PostMapping
    public ResponseEntity<?> testUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "No se recibió ningún archivo"));
            }
            System.out.println("[TEST-UPLOAD] Nombre archivo recibido: " + file.getOriginalFilename());
            return ResponseEntity.ok(Map.of("mensaje", "Archivo recibido correctamente", "nombre", file.getOriginalFilename()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error al recibir archivo: " + e.getMessage()));
        }
    }
}
