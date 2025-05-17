package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.Disponibilidad;

import java.util.List;

public interface DisponibilidadService {
    Disponibilidad crearDisponibilidad(Integer profesionalId, Disponibilidad disponibilidad);
    List<Disponibilidad> obtenerDisponibilidadesPorProfesional(Integer profesionalId);
    void eliminarDisponibilidad(Integer id);
    Disponibilidad obtenerDisponibilidadPorId(Integer id);
}
