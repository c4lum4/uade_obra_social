package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.ObraSocial;
import com.uade.desarrollo.desarrolloAPP.entity.dto.ObraSocialRequest;

import java.util.List;

public interface ObraSocialService {
    ObraSocial createObraSocial(ObraSocialRequest request);
    List<ObraSocial> getAllObrasSociales();
    ObraSocial getObraSocialById(Integer id);
    void deleteObraSocialById(Integer id);
}
