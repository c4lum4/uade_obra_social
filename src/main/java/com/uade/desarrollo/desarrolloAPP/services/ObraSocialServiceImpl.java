package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.ObraSocial;
import com.uade.desarrollo.desarrolloAPP.repository.ObraSocialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObraSocialServiceImpl implements ObraSocialService {

    private final ObraSocialRepository obraSocialRepository;

    public ObraSocialServiceImpl(ObraSocialRepository obraSocialRepository) {
        this.obraSocialRepository = obraSocialRepository;
    }

    @Override
    public ObraSocial createObraSocial(ObraSocial obraSocial) {
        return obraSocialRepository.save(obraSocial);
    }

    @Override
    public List<ObraSocial> getAllObrasSociales() {
        return obraSocialRepository.findAll();
    }

    @Override
    public ObraSocial getObraSocialById(Integer id) {
        Optional<ObraSocial> obraSocial = obraSocialRepository.findById(id);
        return obraSocial.orElse(null);
    }

    @Override
    public void deleteObraSocialById(Integer id) {
        obraSocialRepository.deleteById(id);
    }
}
