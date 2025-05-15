package com.uade.desarrollo.desarrolloAPP.repository;

import com.uade.desarrollo.desarrolloAPP.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByUsuarioId(Long usuarioId);
    List<Notificacion> findByTurnoId(Integer turnoId);
    List<Notificacion> findByUsuarioIdAndTurnoId(Long usuarioId, Integer turnoId);
}
