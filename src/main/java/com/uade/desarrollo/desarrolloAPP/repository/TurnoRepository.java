package com.uade.desarrollo.desarrolloAPP.repository;

import com.uade.desarrollo.desarrolloAPP.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    // Métodos personalizados para Turno si es necesario
}
