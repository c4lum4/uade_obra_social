package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.*;
import com.uade.desarrollo.desarrolloAPP.entity.dto.*;
import com.uade.desarrollo.desarrolloAPP.exceptions.TurnoYaReservadoException;
import com.uade.desarrollo.desarrolloAPP.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TurnoServiceImpl implements TurnoService {
    
    private final TurnoRepository turnoRepository;
    private final UserRepository userRepository;
    private final ProfesionalRepository profesionalRepository;
    private final NotificacionService notificacionService;

    @Override
    public TurnoResponseDTO createTurno(CrearTurnoDTO turnoDTO) {
        Profesional profesional = profesionalRepository.findById(turnoDTO.getProfesionalId())
            .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        if (turnoRepository.existsByFechaAndProfesionalId(turnoDTO.getFecha(), turnoDTO.getProfesionalId())) {
            throw new IllegalStateException("Ya existe un turno para este profesional en la fecha seleccionada");
        }

        Turno nuevoTurno = new Turno();
        nuevoTurno.setFecha(turnoDTO.getFecha());
        nuevoTurno.setProfesional(profesional);
        nuevoTurno.setEstado(turnoDTO.getEstado());

        Turno turnoGuardado = turnoRepository.save(nuevoTurno);
        return convertToDTO(turnoGuardado);
    }

    @Override
    public List<TurnoResponseDTO> getTurnosPorProfesional(Integer profesionalId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Turno> turnos = turnoRepository.findByProfesionalIdAndFechaBetween(profesionalId, fechaInicio, fechaFin);
        return turnos.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public TurnoResponseDTO getTurnoById(Integer id) {
        Turno turno = turnoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        return convertToDTO(turno);
    }

    @Override
    public void deleteTurnoById(Integer id) {
        turnoRepository.deleteById(id);
    }

    @Override
    public TurnoResponseDTO reservarTurno(ReservaTurnoDTO reservaDTO) {
        Turno turno = turnoRepository.findById(reservaDTO.getTurnoId())
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        
        // Verificar si ya hay un turno reservado/completado para ese profesional en esa fecha
        boolean turnoExistente = turnoRepository.existsByProfesionalIdAndFechaAndEstadoIn(
            turno.getProfesional().getId(),
            turno.getFecha(),
            List.of(Turno.EstadoTurno.RESERVADO, Turno.EstadoTurno.COMPLETADO)
        );
        
        if (turnoExistente) {
            throw new TurnoYaReservadoException("Ya existe un turno reservado para este profesional en ese horario");
        }

        if (turno.getEstado() != Turno.EstadoTurno.DISPONIBLE) {
            throw new TurnoYaReservadoException("El turno ya no está disponible");
        }

        User usuario = userRepository.findById(reservaDTO.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        turno.setUsuario(usuario);
        turno.setEstado(Turno.EstadoTurno.RESERVADO);

        Turno turnoActualizado = turnoRepository.save(turno);
        
        // Crear notificación
        String mensaje = "Has reservado un turno para el día " + turnoActualizado.getFecha();
        notificacionService.crearNotificacion(mensaje, turnoActualizado, usuario);

        return convertToDTO(turnoActualizado);
    }

    @Override
    public List<TurnoResponseDTO> listarTurnosDisponiblesPorProfesional(Integer profesionalId) {
        return turnoRepository.findByProfesionalIdAndEstado(profesionalId, Turno.EstadoTurno.DISPONIBLE)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public TurnoResponseDTO cancelarTurno(Integer turnoId, Long usuarioId) {
        Turno turno = turnoRepository.findById(turnoId)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        
        // Guardar el usuario antes de eliminarlo para la notificación
        User usuario = turno.getUsuario();
        
        // Restablecer el turno como disponible
        turno.setEstado(Turno.EstadoTurno.DISPONIBLE);
        turno.setUsuario(null);
        
        Turno turnoActualizado = turnoRepository.save(turno);
        
        // Crear notificación de cancelación
        String mensaje = "Tu turno para el día " + turnoActualizado.getFecha() + " ha sido cancelado";
        notificacionService.crearNotificacion(mensaje, turnoActualizado, usuario);
        
        return convertToDTO(turnoActualizado);
    }

    // Helper para convertir Turno → TurnoResponseDTO
    public TurnoResponseDTO convertToDTO(Turno turno) {
        TurnoResponseDTO dto = new TurnoResponseDTO();
        dto.setId(turno.getId());
        dto.setFecha(turno.getFecha());
        dto.setEstado(turno.getEstado().name());
        if (turno.getProfesional() != null) {
            dto.setProfesionalId(turno.getProfesional().getId());
            dto.setNombreProfesional(turno.getProfesional().getNombre());
            dto.setEspecialidadProfesional(turno.getProfesional().getEspecialidad());
        }
        if (turno.getUsuario() != null) {
            dto.setUsuarioId(turno.getUsuario().getId());
            dto.setNombreUsuario(turno.getUsuario().getName() + " " + turno.getUsuario().getSurname());
        }
        return dto;
    }

    // ——> Helper para convertir Turno → TurnoDTO (para la búsqueda)
    public TurnoDTO convertToTurnoDTO(Turno turno) {
        TurnoDTO dto = new TurnoDTO();
        dto.setId(turno.getId());
        dto.setFecha(turno.getFecha());
        dto.setProfesionalId(turno.getProfesional().getId());
        dto.setNombre_de_profesional(turno.getProfesional().getNombre());
        dto.setEspecialidad_de_profesional(turno.getProfesional().getEspecialidad());
        if (turno.getUsuario() != null) {
            dto.setUsuarioId(turno.getUsuario().getId());
        }
        dto.setEstado(turno.getEstado().name());
        return dto;
    }

    // ——> Implementación del método de búsqueda por nombre de profesional
    @Override
    public List<TurnoDTO> buscarTurnosPorNombreProfesional(String nombreProfesional) {
        List<Turno> turnos = turnoRepository.findByProfesionalNombre(nombreProfesional);
        return turnos.stream()
            .map(this::convertToTurnoDTO)
            .collect(Collectors.toList());
    }
}