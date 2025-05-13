package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.*;
import com.uade.desarrollo.desarrolloAPP.entity.dto.*;
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
        
        if (turno.getEstado() != Turno.EstadoTurno.DISPONIBLE) {
            throw new IllegalStateException("El turno no está disponible para reserva");
        }

        User usuario = userRepository.findById(reservaDTO.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        turno.setUsuario(usuario);
        turno.setEstado(Turno.EstadoTurno.RESERVADO);

        Turno turnoActualizado = turnoRepository.save(turno);
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
        
        if (!turno.getUsuario().getId().equals(usuarioId)) {
            throw new SecurityException("No tienes permiso para cancelar este turno");
        }
        
        turno.setEstado(Turno.EstadoTurno.CANCELADO);
        Turno turnoActualizado = turnoRepository.save(turno);
        return convertToDTO(turnoActualizado);
    }

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
}