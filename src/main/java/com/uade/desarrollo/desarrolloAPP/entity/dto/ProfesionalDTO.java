package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;

@Data
public class ProfesionalDTO {
    private Integer id;
    private String nombre;
    private String especialidad;
    // Si querés también incluir turnos o disponibilidades, avisame y lo agregamos.
}
