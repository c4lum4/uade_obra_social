package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.Notificacion;
import com.uade.desarrollo.desarrolloAPP.entity.Turno;
import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.entity.dto.NotificacionDTO;
import com.uade.desarrollo.desarrolloAPP.repository.TurnoRepository;
import com.uade.desarrollo.desarrolloAPP.repository.UserRepository;
import com.uade.desarrollo.desarrolloAPP.services.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {
    // DELETE /api/notificaciones/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Integer id) {
        try {
            notificacionService.eliminarNotificacionPorId(id);
            return ResponseEntity.ok(Map.of("mensaje", "Notificación eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error al eliminar la notificación: " + e.getMessage()));
        }
    }

    private final NotificacionService notificacionService;
    private final TurnoRepository turnoRepository;
    private final UserRepository userRepository;

    // GET /api/notificaciones/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public List<NotificacionDTO> getNotificacionesPorUsuario(@PathVariable Long usuarioId) {
        List<Notificacion> notificaciones = notificacionService.getNotificacionesPorUsuario(usuarioId);
        return notificaciones.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PutMapping("/usuario/{usuarioId}/marcar-leidas")
    public ResponseEntity<?> marcarNotificacionesComoLeidas(@PathVariable Long usuarioId) {
        notificacionService.marcarTodasComoLeidas(usuarioId);
        // Obtener la lista actualizada de notificaciones del usuario
        List<Notificacion> notificaciones = notificacionService.getNotificacionesPorUsuario(usuarioId);
        List<NotificacionDTO> dtos = notificaciones.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok().body(Map.of(
            "mensaje", "Notificaciones marcadas como leídas",
            "notificaciones", dtos
        ));
    }

    // GET /api/notificaciones
    @GetMapping
    public List<NotificacionDTO> getTodasLasNotificaciones() {
        List<Notificacion> notificaciones = notificacionService.getTodasLasNotificaciones();
        return notificaciones.stream().map(this::convertToDTO).collect(Collectors.toList());
    }    @PostMapping
    public ResponseEntity<?> crearNotificacion(@RequestBody NotificacionDTO dto) {
        try {
            Optional<Turno> turnoOpt = turnoRepository.findById(dto.getTurnoId());
            if (turnoOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "No se encontró el turno especificado"));
            }

            Optional<User> usuarioOpt = userRepository.findById(dto.getUsuarioId());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "No se encontró el usuario especificado"));
            }

            // Validar que el turno corresponda al usuario
            if (!turnoOpt.get().getUsuario().getId().equals(dto.getUsuarioId())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "El turno no corresponde al usuario especificado"));
            }

            Notificacion notificacion = notificacionService.crearNotificacion(
                    dto.getMensaje(),
                    turnoOpt.get(),
                    usuarioOpt.get()
            );

            return ResponseEntity.ok()
                .body(Map.of(
                    "mensaje", "Notificación creada exitosamente",
                    "notificacion", convertToDTO(notificacion)
                ));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("mensaje", "Error al crear la notificación: " + e.getMessage()));
        }
    }

    // GET /api/notificaciones/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificacionById(@PathVariable Integer id) {
        var notificacion = notificacionService.getNotificacionById(id);
        if (notificacion == null) {
            throw new com.uade.desarrollo.desarrolloAPP.exceptions.NotFoundException("No se encontró la notificación con ID: " + id);
        }
        return ResponseEntity.ok(convertToDTO(notificacion));
    }

    private NotificacionDTO convertToDTO(Notificacion notificacion) {
    NotificacionDTO dto = new NotificacionDTO();
    dto.setId(notificacion.getId());
    dto.setMensaje(notificacion.getMensaje());
    dto.setFecha(notificacion.getFecha());
    dto.setTurnoId(notificacion.getTurno().getId());
    dto.setUsuarioId(notificacion.getUsuario().getId());
    dto.setLeida(notificacion.isLeida()); 
    return dto;
}
}
