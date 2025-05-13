package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ReservaTurnoDTO {
    @NotNull
    private Integer turnoId;
    
    @NotNull
    private Long usuarioId;
}