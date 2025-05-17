package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.Disponibilidad;
import com.uade.desarrollo.desarrolloAPP.entity.Profesional;
import com.uade.desarrollo.desarrolloAPP.repository.DisponibilidadRepository;
import com.uade.desarrollo.desarrolloAPP.repository.ProfesionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;
    private final ProfesionalRepository profesionalRepository;

    @Override
    public Disponibilidad crearDisponibilidad(Integer profesionalId, Disponibilidad disponibilidad) {
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));
        disponibilidad.setProfesional(profesional);
        return disponibilidadRepository.save(disponibilidad);
    }

    @Override
    public List<Disponibilidad> obtenerDisponibilidadesPorProfesional(Integer profesionalId) {
        return disponibilidadRepository.findByProfesionalId(profesionalId);
    }

    @Override
    public void eliminarDisponibilidad(Integer id) {
        disponibilidadRepository.deleteById(id);
    }

    @Override
    public Disponibilidad obtenerDisponibilidadPorId(Integer id) {
        return disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
    }
}
