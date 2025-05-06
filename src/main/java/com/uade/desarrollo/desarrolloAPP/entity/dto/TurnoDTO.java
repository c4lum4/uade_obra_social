package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TurnoDTO {
    private Integer id;
    private LocalDateTime fecha;
    private Integer profesionalId;
    private Long usuarioId;
    private String estado;
}
