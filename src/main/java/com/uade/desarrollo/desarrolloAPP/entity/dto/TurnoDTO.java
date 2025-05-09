package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TurnoDTO {
    private Integer id;
    private LocalDateTime fecha;
    private Integer profesionalId;
    private String nombre_de_profesional;
    private String especialidad_de_profesional;
    private Long usuarioId;
    private String estado;
}
