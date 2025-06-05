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
    
    List<Turno> findByEstado(Turno.EstadoTurno estado);
    
    @Query("SELECT t FROM Turno t WHERE t.profesional.id = :profesionalId " +
           "AND (:fechaInicio IS NULL OR t.fecha >= :fechaInicio) " +
           "AND (:fechaFin IS NULL OR t.fecha <= :fechaFin)")
    List<Turno> findByProfesionalIdAndFechaBetween(
        @Param("profesionalId") Integer profesionalId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT t FROM Turno t WHERE t.profesional.especialidad = :especialidad")
    List<Turno> findByEspecialidad(@Param("especialidad") String especialidad);

    
    boolean existsByProfesionalIdAndFechaAndEstadoIn(
        Integer profesionalId, 
        LocalDateTime fecha, 
        List<Turno.EstadoTurno> estados
    );

    // Búsqueda por nombre de profesional (ya existente)
    @Query("SELECT t FROM Turno t JOIN t.profesional p " +
           "WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombreProfesional, '%'))")
    List<Turno> findByProfesionalNombre(@Param("nombreProfesional") String nombreProfesional);

    // ——> Nuevo: búsqueda solo por nombre de obra social
    @Query("SELECT t FROM Turno t " +
           "JOIN t.usuario u " +
           "JOIN u.obrasSociales os " +
           "WHERE LOWER(os.nombreObraSocial) LIKE LOWER(CONCAT('%', :nombreOS, '%'))")
    List<Turno> findByObraSocialNombre(@Param("nombreOS") String nombreOS);

    // Buscar turnos por ID de usuario
    List<Turno> findByUsuarioIdAndEstadoIn(Long usuarioId, List<Turno.EstadoTurno> estados);
}