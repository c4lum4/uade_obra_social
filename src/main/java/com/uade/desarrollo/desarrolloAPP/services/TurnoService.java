package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.Turno;

import java.util.List;

public interface TurnoService {
    Turno createTurno(Turno turno);
    List<Turno> getAllTurnos();
    Turno getTurnoById(Integer id);
    void deleteTurnoById(Integer id);
}
