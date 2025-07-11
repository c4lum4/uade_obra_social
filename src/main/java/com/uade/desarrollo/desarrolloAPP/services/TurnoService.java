package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.dto.TurnoDTO;
import com.uade.desarrollo.desarrolloAPP.entity.dto.TurnoResponseDTO;
import com.uade.desarrollo.desarrolloAPP.entity.dto.CrearTurnoDTO;
import com.uade.desarrollo.desarrolloAPP.entity.dto.ReservaTurnoDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TurnoService {
    TurnoResponseDTO createTurno(CrearTurnoDTO turnoDTO);
    List<TurnoResponseDTO> getTurnosPorProfesional(Integer profesionalId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    TurnoResponseDTO getTurnoById(Integer id);
    void deleteTurnoById(Integer id);
    void generarTurnosDesdeDisponibilidad(Integer profesionalId);
    TurnoResponseDTO reservarTurno(ReservaTurnoDTO reservaDTO);
    List<TurnoResponseDTO> listarTurnosDisponiblesPorProfesional(Integer profesionalId);
    TurnoResponseDTO cancelarTurno(Integer turnoId, Long usuarioId);
    List<TurnoResponseDTO> buscarPorEspecialidad(String especialidad);
    List<TurnoResponseDTO> reservarMultiplesTurnos(List<ReservaTurnoDTO> reservas);
    List<TurnoResponseDTO> getAllTurnos();
    List<TurnoResponseDTO> getTurnosPorUsuario(Long usuarioId, String estado);


    // Búsqueda por nombre de profesional (existente)
    List<TurnoDTO> buscarTurnosPorNombreProfesional(String nombreProfesional);    // ——> Nuevo: búsqueda solo por nombre de obra social
    List<TurnoDTO> buscarTurnosPorObraSocial(String nombreObraSocial);

    // Buscar todos los turnos disponibles
    List<TurnoResponseDTO> getAllTurnosDisponibles();

    // Buscar turnos reservados por un usuario
    List<TurnoResponseDTO> getTurnosReservadosPorUsuario(Long usuarioId);
}
