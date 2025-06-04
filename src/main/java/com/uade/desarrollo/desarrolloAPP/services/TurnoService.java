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
    TurnoResponseDTO reservarTurno(ReservaTurnoDTO reservaDTO);
    List<TurnoResponseDTO> listarTurnosDisponiblesPorProfesional(Integer profesionalId);
    TurnoResponseDTO cancelarTurno(Integer turnoId, Long usuarioId);
    
    // ——> Firma del método nuevo:
    List<TurnoDTO> buscarTurnosPorNombreProfesional(String nombreProfesional);
}