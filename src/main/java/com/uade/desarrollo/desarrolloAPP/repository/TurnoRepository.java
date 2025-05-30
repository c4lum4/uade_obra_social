package com.uade.desarrollo.desarrolloAPP.repository;

import com.uade.desarrollo.desarrolloAPP.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    boolean existsByFechaAndProfesionalId(LocalDateTime fecha, Integer profesionalId);
    
    List<Turno> findByProfesionalIdAndEstado(Integer profesionalId, Turno.EstadoTurno estado);
    
    @Query("SELECT t FROM Turno t WHERE t.profesional.id = :profesionalId " +
        "AND (:fechaInicio IS NULL OR t.fecha >= :fechaInicio) " +
        "AND (:fechaFin IS NULL OR t.fecha <= :fechaFin)")
    List<Turno> findByProfesionalIdAndFechaBetween(
        @Param("profesionalId") Integer profesionalId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin);
    
    boolean existsByProfesionalIdAndFechaAndEstadoIn(
        Integer profesionalId, 
        LocalDateTime fecha, 
        List<Turno.EstadoTurno> estados
    );
}
