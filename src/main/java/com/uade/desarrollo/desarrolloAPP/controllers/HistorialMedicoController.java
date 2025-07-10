package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.dto.HistorialMedicoDTO;
import com.uade.desarrollo.desarrolloAPP.entity.HistorialMedico;
import com.uade.desarrollo.desarrolloAPP.repository.HistorialMedicoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/historial-medico")
public class HistorialMedicoController {

    private final HistorialMedicoRepository repo;

    public HistorialMedicoController(HistorialMedicoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<HistorialMedicoDTO> getAll() {
        return repo.findAll().stream()
            .map(this::toDto)
            .toList();
    }

    private HistorialMedicoDTO toDto(HistorialMedico h) {
        return new HistorialMedicoDTO(
            h.getId(),
            h.getFecha(),
            h.getProfesional(),
            h.getMotivo(),
            h.getFotoUrl(),
            h.getDescripcion(),
            h.getUsuario().getId()
        );
    }
}