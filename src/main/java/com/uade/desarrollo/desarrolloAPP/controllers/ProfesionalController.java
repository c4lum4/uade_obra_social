package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.Profesional;
import com.uade.desarrollo.desarrolloAPP.entity.dto.ProfesionalDTO;
import com.uade.desarrollo.desarrolloAPP.services.ProfesionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profesionales")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    public ProfesionalController(ProfesionalService profesionalService) {
        this.profesionalService = profesionalService;
    }

    @PostMapping
    public ResponseEntity<ProfesionalDTO> createProfesional(@RequestBody Profesional profesional) {
        try {
            Profesional createdProfesional = profesionalService.createProfesional(profesional);
            ProfesionalDTO dto = convertToDTO(createdProfesional);
            return ResponseEntity.created(URI.create("/api/profesionales/" + dto.getId())).body(dto);
        } catch (Exception e) {
            throw new com.uade.desarrollo.desarrolloAPP.exceptions.BusinessException("Error al crear profesional");
        }
    }

    @GetMapping
    public ResponseEntity<List<ProfesionalDTO>> getAllProfesionales() {
        List<ProfesionalDTO> profesionales = profesionalService.getAllProfesionales()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(profesionales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> getProfesionalById(@PathVariable Integer id) {
        Profesional profesional = profesionalService.getProfesionalById(id);
        return ResponseEntity.ok(convertToDTO(profesional));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesionalById(@PathVariable Integer id) {
        profesionalService.deleteProfesionalById(id);
        return ResponseEntity.noContent().build();
    }

    private ProfesionalDTO convertToDTO(Profesional profesional) {
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setId(profesional.getId());
        dto.setNombre(profesional.getNombre());
        dto.setEspecialidad(profesional.getEspecialidad());
        return dto;
    }
}
