package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.Turno;
import com.uade.desarrollo.desarrolloAPP.entity.dto.*;
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
}