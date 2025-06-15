package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionDTO {
    private Integer id;
    private String mensaje;
    private LocalDateTime fecha;
    private Integer turnoId;
    private Long usuarioId;
    private boolean leida; 
}
