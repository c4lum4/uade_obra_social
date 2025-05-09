package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.Turno;
import com.uade.desarrollo.desarrolloAPP.services.TurnoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @PostMapping
    public ResponseEntity<Object> createTurno(@RequestBody Turno turno) {
        Turno createdTurno = turnoService.createTurno(turno);

        // Crear una respuesta detallada con información del turno, profesional y usuario
        Map<String, Object> response = new HashMap<>();
        response.put("id", createdTurno.getId());
        response.put("fecha", createdTurno.getFecha());
        response.put("estado", createdTurno.getEstado());

        // Información del profesional
        Map<String, Object> profesionalInfo = new HashMap<>();
        profesionalInfo.put("id", createdTurno.getProfesional().getId());
        profesionalInfo.put("nombre", createdTurno.getProfesional().getNombre());
        profesionalInfo.put("especialidad", createdTurno.getProfesional().getEspecialidad());
        response.put("profesional", profesionalInfo);

        // Información del usuario
        Map<String, Object> usuarioInfo = new HashMap<>();
        usuarioInfo.put("id", createdTurno.getUsuario().getId());
        usuarioInfo.put("username", createdTurno.getUsuario().getUsername());
        usuarioInfo.put("email", createdTurno.getUsuario().getEmail());
        usuarioInfo.put("name", createdTurno.getUsuario().getName());
        usuarioInfo.put("surname", createdTurno.getUsuario().getSurname());
        response.put("usuario", usuarioInfo);

        return ResponseEntity.created(URI.create("/api/turnos/" + createdTurno.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Turno>> getAllTurnos() {
        return ResponseEntity.ok(turnoService.getAllTurnos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turno> getTurnoById(@PathVariable Integer id) {
        Turno turno = turnoService.getTurnoById(id);
        if (turno == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(turno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurnoById(@PathVariable Integer id) {
        turnoService.deleteTurnoById(id);
        return ResponseEntity.noContent().build();
    }
}
