package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.Disponibilidad;
import com.uade.desarrollo.desarrolloAPP.entity.dto.DisponibilidadDTO;
import com.uade.desarrollo.desarrolloAPP.services.DisponibilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disponibilidades")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    @PostMapping("/{profesionalId}")
    public DisponibilidadDTO crearDisponibilidad(@PathVariable Integer profesionalId, @RequestBody DisponibilidadDTO dto) {
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFecha(dto.getFecha());
        Disponibilidad guardada = disponibilidadService.crearDisponibilidad(profesionalId, disponibilidad);
        return toDTO(guardada);
    }

    @GetMapping("/profesional/{profesionalId}")
    public List<DisponibilidadDTO> obtenerDisponibilidadesPorProfesional(@PathVariable Integer profesionalId) {
        return disponibilidadService.obtenerDisponibilidadesPorProfesional(profesionalId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        disponibilidadService.eliminarDisponibilidad(id);
    }

    private DisponibilidadDTO toDTO(Disponibilidad d) {
        DisponibilidadDTO dto = new DisponibilidadDTO();
        dto.setId(d.getId());
        dto.setFecha(d.getFecha());
        dto.setProfesionalId(d.getProfesional().getId());
        return dto;
    }
}
