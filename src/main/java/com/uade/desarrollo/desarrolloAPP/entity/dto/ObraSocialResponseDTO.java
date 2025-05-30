package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ObraSocialResponseDTO {
    private Integer id;
    private String nombreObraSocial;
    private Long numeroAfiliado;
    private String tipoAfiliado;
    private LocalDate fechaAlta;
    private Long userId;
    
    public static ObraSocialResponseDTO fromEntity(com.uade.desarrollo.desarrolloAPP.entity.ObraSocial obraSocial) {
        ObraSocialResponseDTO dto = new ObraSocialResponseDTO();
        dto.setId(obraSocial.getId());
        dto.setNombreObraSocial(obraSocial.getNombreObraSocial());
        dto.setNumeroAfiliado(obraSocial.getNumeroAfiliado());
        dto.setTipoAfiliado(obraSocial.getTipoAfiliado());
        dto.setFechaAlta(obraSocial.getFechaAlta());
        dto.setUserId(obraSocial.getUser().getId());
        return dto;
    }
}
