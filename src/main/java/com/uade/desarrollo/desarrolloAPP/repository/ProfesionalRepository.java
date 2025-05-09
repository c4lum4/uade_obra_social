package com.uade.desarrollo.desarrolloAPP.repository;

import com.uade.desarrollo.desarrolloAPP.entity.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesionalRepository extends JpaRepository<Profesional, Integer> {
    // Métodos personalizados para Profesional si es necesario
}
