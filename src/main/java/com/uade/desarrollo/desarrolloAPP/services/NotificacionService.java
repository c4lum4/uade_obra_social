package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.Notificacion;
import com.uade.desarrollo.desarrolloAPP.entity.Turno;
import com.uade.desarrollo.desarrolloAPP.entity.User;

import java.util.List;

public interface NotificacionService {
    Notificacion crearNotificacion(String mensaje, Turno turno, User usuario);

    List<Notificacion> getNotificacionesPorUsuario(Long usuarioId);
    List<Notificacion> getTodasLasNotificaciones();
    Notificacion getNotificacionById(Integer id);
    void deleteNotificacionById(Integer id);
    List<Notificacion> getNotificacionesPorTurno(Integer turnoId);
    List<Notificacion> getNotificacionesPorUsuarioYTurno(Long usuarioId, Integer turnoId);

}