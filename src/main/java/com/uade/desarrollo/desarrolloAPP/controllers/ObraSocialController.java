package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.ObraSocial;
import com.uade.desarrollo.desarrolloAPP.entity.dto.ObraSocialRequest;
import com.uade.desarrollo.desarrolloAPP.entity.dto.ObraSocialResponseDTO;
import com.uade.desarrollo.desarrolloAPP.services.ObraSocialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/obras-sociales")
public class ObraSocialController {

    private final ObraSocialService obraSocialService;

    public ObraSocialController(ObraSocialService obraSocialService) {
        this.obraSocialService = obraSocialService;
    }

    @PostMapping
    public ResponseEntity<?> createObraSocial(@RequestBody ObraSocialRequest request) {
        try {
            ObraSocial created = obraSocialService.createObraSocial(request);
            return ResponseEntity.created(URI.create("/api/obras-sociales/" + created.getId()))
                .body(Map.of(
                    "mensaje", "Obra social creada exitosamente",
                    "obraSocial", created
                ));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ObraSocial>> getAllObrasSociales() {
        return ResponseEntity.ok(obraSocialService.getAllObrasSociales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObraSocial> getObraSocialById(@PathVariable Integer id) {
        ObraSocial obraSocial = obraSocialService.getObraSocialById(id);
        if (obraSocial == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(obraSocial);
    }    @GetMapping("/usuario/{userId}")
    public ResponseEntity<?> getObraSocialByUserId(@PathVariable Long userId) {
        try {
            ObraSocialResponseDTO obraSocial = obraSocialService.getObraSocialByUserId(userId);
            return ResponseEntity.ok(obraSocial);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteObraSocialById(@PathVariable Integer id) {
        ObraSocial obraSocial = obraSocialService.getObraSocialById(id);
        if (obraSocial == null) {
            throw new com.uade.desarrollo.desarrolloAPP.exceptions.NotFoundException("No existe esa obra social.");
        }
        obraSocialService.deleteObraSocialById(id);
        return ResponseEntity.ok("Obra social eliminada correctamente.");
    }
}
