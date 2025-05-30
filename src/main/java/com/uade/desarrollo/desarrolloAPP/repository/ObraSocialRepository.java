package com.uade.desarrollo.desarrolloAPP.repository;

import com.uade.desarrollo.desarrolloAPP.entity.ObraSocial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObraSocialRepository extends JpaRepository<ObraSocial, Integer> {
    boolean existsByUserId(Long userId);
    Optional<ObraSocial> findByUserId(Long userId);
}
