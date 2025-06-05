package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.Profesional;
import com.uade.desarrollo.desarrolloAPP.repository.ProfesionalRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfesionalServiceImpl implements ProfesionalService {

    private final ProfesionalRepository profesionalRepository;

    public ProfesionalServiceImpl(ProfesionalRepository profesionalRepository) {
        this.profesionalRepository = profesionalRepository;
    }

    @Override
    public Profesional createProfesional(Profesional profesional) {
        return profesionalRepository.save(profesional);
    }

    @Override
    public List<Profesional> getAllProfesionales() {
        return profesionalRepository.findAll();
    }

    @Override
    public Profesional getProfesionalById(Integer id) {
        Optional<Profesional> profesional = profesionalRepository.findById(id);
        return profesional.orElseThrow(() -> new RuntimeException("Profesional no encontrado"));
    }

    @Override
    public void deleteProfesionalById(Integer id) {
        profesionalRepository.deleteById(id);
    }
}
