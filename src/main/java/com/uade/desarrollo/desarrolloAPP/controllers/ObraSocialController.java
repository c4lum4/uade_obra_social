package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.ObraSocial;
import com.uade.desarrollo.desarrolloAPP.entity.dto.ObraSocialRequest;
import com.uade.desarrollo.desarrolloAPP.services.ObraSocialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/obras-sociales")
public class ObraSocialController {

    private final ObraSocialService obraSocialService;

    public ObraSocialController(ObraSocialService obraSocialService) {
        this.obraSocialService = obraSocialService;
    }

    @PostMapping
    public ResponseEntity<ObraSocial> createObraSocial(@RequestBody ObraSocialRequest request) {
        try {
            ObraSocial created = obraSocialService.createObraSocial(request);
            return ResponseEntity.created(URI.create("/api/obras-sociales/" + created.getId())).body(created);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear obra social");
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
            throw new RuntimeException("Obra social no encontrada");
        }
        return ResponseEntity.ok(obraSocial);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteObraSocialById(@PathVariable Integer id) {
        ObraSocial obraSocial = obraSocialService.getObraSocialById(id);
        if (obraSocial == null) {
            throw new RuntimeException("No existe esa obra social.");
        }
        obraSocialService.deleteObraSocialById(id);
        return ResponseEntity.ok("Obra social eliminada correctamente.");
    }
}
