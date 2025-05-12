package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ObraSocialRequest {
    private String nombreObraSocial;
    private Long numeroAfiliado;
    private String tipoAfiliado;
    private LocalDate fechaAlta;
    private Long userId;
}
