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
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TurnoController {

    private final TurnoService turnoService;

    @PostMapping
    public ResponseEntity<TurnoResponseDTO> createTurno(@Valid @RequestBody CrearTurnoDTO turnoDTO) {
        try {
            var response = turnoService.createTurno(turnoDTO);
            return ResponseEntity.created(URI.create("/api/turnos/" + response.getId()))
                    .body(response);
        } catch (Exception e) {
            throw new com.uade.desarrollo.desarrolloAPP.exceptions.BusinessException("Error al crear turno");
        }
    }

    @GetMapping("/profesional/{profesionalId}")
    public ResponseEntity<List<TurnoResponseDTO>> getTurnosPorProfesional(
            @PathVariable Integer profesionalId,
            @RequestParam(required = false) LocalDateTime fechaInicio,
            @RequestParam(required = false) LocalDateTime fechaFin) {

        var lista = turnoService.getTurnosPorProfesional(profesionalId, fechaInicio, fechaFin);
        return ResponseEntity.ok(lista);
    }

    // ...existing code...

@GetMapping("/todos")
public ResponseEntity<List<TurnoResponseDTO>> getAllTurnos() {
    var turnos = turnoService.getAllTurnos();
    return ResponseEntity.ok(turnos);
}

// ...existing code...




@GetMapping("/usuario/{usuarioId}/todos")
public ResponseEntity<?> getTodosLosTurnosPorUsuario(
        @PathVariable Long usuarioId,
        @RequestParam(required = false) String estado) {
    try {
        var turnos = turnoService.getTurnosPorUsuario(usuarioId, estado);
        return ResponseEntity.ok(turnos);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("mensaje", "Error al obtener los turnos: " + e.getMessage()));
    }
}

   

@PostMapping("/reservar-multiple")
public ResponseEntity<?> reservarMultiplesTurnos(@RequestBody List<ReservaTurnoDTO> reservas) {
    try {
        var turnos = turnoService.reservarMultiplesTurnos(reservas);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Turnos reservados exitosamente",
                "turnos", turnos
        ));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", "No se pudieron reservar los turnos: " + e.getMessage()));
    }
}



    @PostMapping("/generar-turnos")
    public ResponseEntity<?> generarTurnosDesdeDisponibilidad(@RequestBody GenerarTurnosDTO dto) {
    try {
        turnoService.generarTurnosDesdeDisponibilidad(dto.getProfesionalId());
        return ResponseEntity.ok(Map.of("mensaje", "Turnos generados exitosamente"));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("mensaje", "Error al generar turnos: " + e.getMessage()));
    }
}

    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> getTurnoById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(turnoService.getTurnoById(id));
        } catch (Exception e) {
            throw new com.uade.desarrollo.desarrolloAPP.exceptions.NotFoundException("Turno no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurnoById(@PathVariable Integer id) {
        try {
            turnoService.deleteTurnoById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new com.uade.desarrollo.desarrolloAPP.exceptions.BusinessException("No se pudo eliminar el turno");
        }
    }

    @PostMapping("/reservar")
    public ResponseEntity<?> reservarTurno(@Valid @RequestBody ReservaTurnoDTO reservaDTO) {
        try {
            var turno = turnoService.reservarTurno(reservaDTO);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Turno reservado exitosamente",
                    "turno", turno
            ));
        } catch (TurnoYaReservadoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "No se pudo reservar el turno"));
        }
    }

    @GetMapping("/disponibles/{profesionalId}")
    public ResponseEntity<List<TurnoResponseDTO>> listarTurnosDisponibles(
            @PathVariable Integer profesionalId) {
        var lista = turnoService.listarTurnosDisponiblesPorProfesional(profesionalId);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/cancelar/{turnoId}")
    public ResponseEntity<?> cancelarTurno(
            @PathVariable Integer turnoId,
            @RequestParam Long usuarioId) {
        try {
            var turno = turnoService.cancelarTurno(turnoId, usuarioId);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Turno cancelado exitosamente",
                    "turno", turno
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "No se pudo cancelar el turno: " + e.getMessage()));
        }
    }

    // Búsqueda solo por nombre de profesional (existente)
    @GetMapping("/buscar/profesional")
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET})
    public ResponseEntity<List<TurnoDTO>> buscarTurnosPorNombreProfesional(
            @RequestParam(name = "nombre", required = true) String nombre) {
        try {
            var turnos = turnoService.buscarTurnosPorNombreProfesional(nombre);
            return ResponseEntity.ok().header("Access-Control-Allow-Origin", "*").body(turnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<TurnoResponseDTO>> buscarDisponiblesPorEspecialidad(@PathVariable String especialidad) {
    return ResponseEntity.ok(turnoService.buscarPorEspecialidad(especialidad));
}
    // ——> Nuevo endpoint: buscar solo por nombre de obra social    @GetMapping("/buscar/obra-social")
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET})
    public ResponseEntity<List<TurnoDTO>> buscarTurnosPorObraSocial(
            @RequestParam(name = "obraSocial", required = true) String obraSocial) {
        try {
            var turnos = turnoService.buscarTurnosPorObraSocial(obraSocial);
            return ResponseEntity.ok().header("Access-Control-Allow-Origin", "*").body(turnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene todos los turnos que están en estado DISPONIBLE
     * @return Lista de turnos disponibles o mensaje de error si ocurre algún problema
     */
    @GetMapping("/disponibles")
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET})
    public ResponseEntity<?> getAllTurnosDisponibles() {
        try {
            var turnos = turnoService.getAllTurnosDisponibles();
            if (turnos.isEmpty()) {
                return ResponseEntity.ok()
                    .body(Map.of("mensaje", "No hay turnos disponibles en este momento"));
            }
            return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .body(turnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("mensaje", "Error al obtener los turnos disponibles: " + e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getTurnosReservadosPorUsuario(@PathVariable Long usuarioId) {
        try {
            var turnos = turnoService.getTurnosReservadosPorUsuario(usuarioId);
            return ResponseEntity.ok(turnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error al obtener los turnos: " + e.getMessage()));
        }
    }
}