package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.ObraSocial;
import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.entity.dto.ObraSocialRequest;
import com.uade.desarrollo.desarrolloAPP.exceptions.ObraSocialDuplicadaException;
import com.uade.desarrollo.desarrolloAPP.repository.ObraSocialRepository;
import com.uade.desarrollo.desarrolloAPP.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObraSocialServiceImpl implements ObraSocialService {

    private final ObraSocialRepository obraSocialRepository;
    private final UserRepository userRepository;

    public ObraSocialServiceImpl(ObraSocialRepository obraSocialRepository, UserRepository userRepository) {
        this.obraSocialRepository = obraSocialRepository;
        this.userRepository = userRepository;
    }    @Override
    public ObraSocial createObraSocial(ObraSocialRequest request) {
        Optional<User> userOptional = userRepository.findById(request.getUserId());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + request.getUserId());
        }

        // Verificar si el usuario ya tiene una obra social
        if (obraSocialRepository.existsByUserId(request.getUserId())) {
            throw new ObraSocialDuplicadaException("El usuario ya tiene una obra social asociada");
        }

        ObraSocial obraSocial = ObraSocial.builder()
                .nombreObraSocial(request.getNombreObraSocial())
                .numeroAfiliado(request.getNumeroAfiliado())
                .tipoAfiliado(request.getTipoAfiliado())
                .fechaAlta(request.getFechaAlta())
                .user(userOptional.get())
                .build();

        return obraSocialRepository.save(obraSocial);
    }

    @Override
    public List<ObraSocial> getAllObrasSociales() {
        return obraSocialRepository.findAll();
    }

    @Override
    public ObraSocial getObraSocialById(Integer id) {
        return obraSocialRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteObraSocialById(Integer id) {
        obraSocialRepository.deleteById(id);
    }
}
