package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TurnoResponseDTO {
    private Integer id;
    private LocalDateTime fecha;
    private String estado;
    private Integer profesionalId;
    private String nombreProfesional;
    private String especialidadProfesional;
    private Long usuarioId;
    private String nombreUsuario;
}