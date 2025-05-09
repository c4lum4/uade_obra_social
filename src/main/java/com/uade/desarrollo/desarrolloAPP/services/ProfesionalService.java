package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.Profesional;

import java.util.List;

public interface ProfesionalService {
    Profesional createProfesional(Profesional profesional);
    List<Profesional> getAllProfesionales();
    Profesional getProfesionalById(Integer id);
    void deleteProfesionalById(Integer id);
}
