package com.uade.desarrollo.desarrolloAPP.repository;

import com.uade.desarrollo.desarrolloAPP.entity.ObraSocial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObraSocialRepository extends JpaRepository<ObraSocial, Integer> {
    // Métodos personalizados para ObraSocial si es necesario
}
