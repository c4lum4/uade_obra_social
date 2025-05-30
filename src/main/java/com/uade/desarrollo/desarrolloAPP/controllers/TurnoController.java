package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.dto.*;
import com.uade.desarrollo.desarrolloAPP.exceptions.TurnoYaReservadoException;
import com.uade.desarrollo.desarrolloAPP.services.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    @PostMapping
    public ResponseEntity<TurnoResponseDTO> createTurno(@Valid @RequestBody CrearTurnoDTO turnoDTO) {
        try {
            TurnoResponseDTO response = turnoService.createTurno(turnoDTO);
            return ResponseEntity.created(URI.create("/api/turnos/" + response.getId()))
                .body(response);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear turno");
        }
    }

    @GetMapping("/profesional/{profesionalId}")
    public ResponseEntity<List<TurnoResponseDTO>> getTurnosPorProfesional(
        @PathVariable Integer profesionalId,
        @RequestParam(required = false) LocalDateTime fechaInicio,
        @RequestParam(required = false) LocalDateTime fechaFin) {
        
        return ResponseEntity.ok(turnoService.getTurnosPorProfesional(profesionalId, fechaInicio, fechaFin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> getTurnoById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(turnoService.getTurnoById(id));
        } catch (Exception e) {
            throw new RuntimeException("Turno no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurnoById(@PathVariable Integer id) {
        try {
            turnoService.deleteTurnoById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar el turno");
        }
    }    @PostMapping("/reservar")
    public ResponseEntity<?> reservarTurno(@Valid @RequestBody ReservaTurnoDTO reservaDTO) {
        try {
            TurnoResponseDTO turno = turnoService.reservarTurno(reservaDTO);
            return ResponseEntity.ok()
                .body(Map.of(
                    "mensaje", "Turno reservado exitosamente",
                    "turno", turno
                ));
        } catch (TurnoYaReservadoException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", "No se pudo reservar el turno"));
        }
    }
    
    @GetMapping("/disponibles/{profesionalId}")
    public ResponseEntity<List<TurnoResponseDTO>> listarTurnosDisponibles(
            @PathVariable Integer profesionalId) {
        return ResponseEntity.ok(turnoService.listarTurnosDisponiblesPorProfesional(profesionalId));
    }      @PostMapping("/cancelar/{turnoId}")
    public ResponseEntity<?> cancelarTurno(
            @PathVariable Integer turnoId,
            @RequestParam Long usuarioId) {
        try {
            TurnoResponseDTO turno = turnoService.cancelarTurno(turnoId, usuarioId);
            return ResponseEntity.ok()
                .body(Map.of(
                    "mensaje", "Turno cancelado exitosamente",
                    "turno", turno
                ));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", "No se pudo cancelar el turno: " + e.getMessage()));
        }
    }
}