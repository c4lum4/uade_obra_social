package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DisponibilidadDTO {
    private Integer id;
    private LocalDateTime fecha;
    private Integer profesionalId; // solo el ID del profesional
}
