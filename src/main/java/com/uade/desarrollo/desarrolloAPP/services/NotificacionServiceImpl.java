package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.Notificacion;
import com.uade.desarrollo.desarrolloAPP.entity.Turno;
import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Override
    public Notificacion crearNotificacion(String mensaje, Turno turno, User usuario) {
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(mensaje);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setTurno(turno);
        notificacion.setUsuario(usuario);
        return notificacionRepository.save(notificacion);
    }
    @Override
    public void marcarTodasComoLeidas(Long usuarioId) {
    List<Notificacion> notificaciones = notificacionRepository.findByUsuarioId(usuarioId);
    for (Notificacion n : notificaciones) {
        n.setLeida(true);
    }
    notificacionRepository.saveAll(notificaciones);
}

    @Override
    public List<Notificacion> getNotificacionesPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Notificacion> getTodasLasNotificaciones() {
        return notificacionRepository.findAll();
    }

    @Override
    public Notificacion getNotificacionById(Integer id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new com.uade.desarrollo.desarrolloAPP.exceptions.NotFoundException("No se encontró la notificación con ID: " + id));
    }

    @Override
    public void eliminarNotificacionPorId(Integer id) {
        notificacionRepository.deleteById(id);
    }

    @Override
    public List<Notificacion> getNotificacionesPorTurno(Integer turnoId) {
        return notificacionRepository.findByTurnoId(turnoId);
    }

    @Override
    public List<Notificacion> getNotificacionesPorUsuarioYTurno(Long usuarioId, Integer turnoId) {
        return notificacionRepository.findByUsuarioIdAndTurnoId(usuarioId, turnoId);
    }
}
