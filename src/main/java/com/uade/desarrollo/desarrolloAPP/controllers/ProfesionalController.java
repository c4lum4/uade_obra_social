package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.Profesional;
import com.uade.desarrollo.desarrolloAPP.services.ProfesionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    public ProfesionalController(ProfesionalService profesionalService) {
        this.profesionalService = profesionalService;
    }

    @PostMapping
    public ResponseEntity<Profesional> createProfesional(@RequestBody Profesional profesional) {
        Profesional createdProfesional = profesionalService.createProfesional(profesional);
        return ResponseEntity.created(URI.create("/api/profesionales/" + createdProfesional.getId())).body(createdProfesional);
    }

    @GetMapping
    public ResponseEntity<List<Profesional>> getAllProfesionales() {
        return ResponseEntity.ok(profesionalService.getAllProfesionales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesional> getProfesionalById(@PathVariable Integer id) {
        Profesional profesional = profesionalService.getProfesionalById(id);
        if (profesional == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profesional);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesionalById(@PathVariable Integer id) {
        profesionalService.deleteProfesionalById(id);
        return ResponseEntity.noContent().build();
    }
}
