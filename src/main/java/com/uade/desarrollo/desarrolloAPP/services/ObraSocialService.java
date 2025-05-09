package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.ObraSocial;

import java.util.List;

public interface ObraSocialService {
    ObraSocial createObraSocial(ObraSocial obraSocial);
    List<ObraSocial> getAllObrasSociales();
    ObraSocial getObraSocialById(Integer id);
    void deleteObraSocialById(Integer id);
}
