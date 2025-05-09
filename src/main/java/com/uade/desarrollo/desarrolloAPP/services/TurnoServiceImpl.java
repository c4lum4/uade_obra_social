package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.Turno;
import com.uade.desarrollo.desarrolloAPP.repository.TurnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;

    public TurnoServiceImpl(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @Override
    public Turno createTurno(Turno turno) {
        return turnoRepository.save(turno);
    }

    @Override
    public List<Turno> getAllTurnos() {
        return turnoRepository.findAll();
    }

    @Override
    public Turno getTurnoById(Integer id) {
        Optional<Turno> turno = turnoRepository.findById(id);
        return turno.orElse(null);
    }

    @Override
    public void deleteTurnoById(Integer id) {
        turnoRepository.deleteById(id);
    }
}
