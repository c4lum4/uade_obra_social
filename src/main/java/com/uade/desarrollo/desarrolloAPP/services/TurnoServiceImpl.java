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
        var profesional = profesionalRepository.findById(turnoDTO.getProfesionalId())
            .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        if (turnoRepository.existsByFechaAndProfesionalId(turnoDTO.getFecha(), turnoDTO.getProfesionalId())) {
            throw new IllegalStateException("Ya existe un turno para este profesional en la fecha seleccionada");
        }

        Turno nuevoTurno = new Turno();
        nuevoTurno.setFecha(turnoDTO.getFecha());
        nuevoTurno.setProfesional(profesional);
        nuevoTurno.setEstado(turnoDTO.getEstado());

        var turnoGuardado = turnoRepository.save(nuevoTurno);
        return convertToDTO(turnoGuardado);
    }
    @Override
    public void generarTurnosDesdeDisponibilidad(Integer profesionalId) {
    Profesional profesional = profesionalRepository.findById(profesionalId)
        .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

    List<Disponibilidad> disponibilidades = profesional.getDisponibilidades();

    for (Disponibilidad disponibilidad : disponibilidades) {
        LocalDateTime inicio = disponibilidad.getFecha();
        LocalDateTime fin = inicio.plusHours(1); // Asumimos 1 hora por disponibilidad
        LocalDateTime actual = inicio;

        while (actual.isBefore(fin)) {
            // Validar que no exista ya un turno en ese horario
            boolean existe = turnoRepository.existsByFechaAndProfesionalId(actual, profesionalId);
            if (!existe) {
                Turno turno = new Turno();
                turno.setFecha(actual);
                turno.setProfesional(profesional);
                turno.setEstado(Turno.EstadoTurno.DISPONIBLE);
                turnoRepository.save(turno);
            }
            actual = actual.plusMinutes(30);
        }
    }
}


    @Override
    public List<TurnoResponseDTO> getTurnosPorProfesional(Integer profesionalId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        var turnos = turnoRepository.findByProfesionalIdAndFechaBetween(profesionalId, fechaInicio, fechaFin);
        return turnos.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public TurnoResponseDTO getTurnoById(Integer id) {
        var turno = turnoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        return convertToDTO(turno);
    }
    @Override
    public List<TurnoResponseDTO> buscarPorEspecialidad(String especialidad) {
    List<Turno> turnos = turnoRepository.findByEspecialidad(especialidad);
    return turnos.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
}

    @Override
    public void deleteTurnoById(Integer id) {
        turnoRepository.deleteById(id);
    }

    @Override
    public TurnoResponseDTO reservarTurno(ReservaTurnoDTO reservaDTO) {
        var turno = turnoRepository.findById(reservaDTO.getTurnoId())
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

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

        var usuario = userRepository.findById(reservaDTO.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        turno.setUsuario(usuario);
        turno.setEstado(Turno.EstadoTurno.RESERVADO);

        var turnoActualizado = turnoRepository.save(turno);

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
        var turno = turnoRepository.findById(turnoId)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        var usuario = turno.getUsuario();

        turno.setEstado(Turno.EstadoTurno.DISPONIBLE);
        turno.setUsuario(null);

        var turnoActualizado = turnoRepository.save(turno);

        String mensaje = "Tu turno para el día " + turnoActualizado.getFecha() + " ha sido cancelado";
        notificacionService.crearNotificacion(mensaje, turnoActualizado, usuario);

        return convertToDTO(turnoActualizado);
    }

    // Helper para convertir Turno → TurnoResponseDTO
    public TurnoResponseDTO convertToDTO(Turno turno) {
        var dto = new TurnoResponseDTO();
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

    // Helper para convertir Turno → TurnoDTO (para búsquedas)
    public TurnoDTO convertToTurnoDTO(Turno turno) {
        var dto = new TurnoDTO();
        dto.setId(turno.getId());
        dto.setFecha(turno.getFecha());
        dto.setProfesionalId(turno.getProfesional().getId());
        dto.setNombre_de_profesional(turno.getProfesional().getNombre());
        dto.setEspecialidad_de_profesional(turno.getProfesional().getEspecialidad());
        if (turno.getUsuario() != null) {
            dto.setUsuarioId(turno.getUsuario().getId());
            dto.setEstado(turno.getEstado().name());
            // Tomamos la primera obra social (cada usuario solo tiene una, en tu modelo)
            var obras = turno.getUsuario().getObrasSociales();
            if (obras != null && !obras.isEmpty()) {
                dto.setObraSocialNombre(obras.get(0).getNombreObraSocial());
            }
        } else {
            dto.setEstado(turno.getEstado().name());
        }
        return dto;
    }

    // Búsqueda solo por nombre de profesional (existente)
    @Override
    public List<TurnoDTO> buscarTurnosPorNombreProfesional(String nombreProfesional) {
        var turnos = turnoRepository.findByProfesionalNombre(nombreProfesional);
        return turnos.stream()
            .map(this::convertToTurnoDTO)
            .collect(Collectors.toList());
    }    // ——> Búsqueda solo por nombre de obra social (nuevo)    @Override
    public List<TurnoDTO> buscarTurnosPorObraSocial(String nombreObraSocial) {
        var turnos = turnoRepository.findByObraSocialNombre(nombreObraSocial);
        return turnos.stream()
            .map(this::convertToTurnoDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los turnos que están en estado DISPONIBLE
     * @return Lista de turnos disponibles convertidos a DTOs
     */
    @Override
    public List<TurnoResponseDTO> getAllTurnosDisponibles() {
        return turnoRepository.findByEstado(Turno.EstadoTurno.DISPONIBLE)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<TurnoResponseDTO> getTurnosReservadosPorUsuario(Long usuarioId) {
        var estados = List.of(Turno.EstadoTurno.RESERVADO, Turno.EstadoTurno.COMPLETADO);
        var turnos = turnoRepository.findByUsuarioIdAndEstadoIn(usuarioId, estados);
        return turnos.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
}