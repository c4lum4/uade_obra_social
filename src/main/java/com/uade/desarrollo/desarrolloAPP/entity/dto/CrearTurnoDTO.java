package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

import com.uade.desarrollo.desarrolloAPP.entity.Turno;

import jakarta.validation.constraints.NotNull;

@Data
public class CrearTurnoDTO {
    @Future(message = "La fecha debe ser en el futuro")
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;
    
    @NotNull(message = "El ID del profesional es obligatorio")
    private Integer profesionalId;
    
    private Turno.EstadoTurno estado = Turno.EstadoTurno.DISPONIBLE;
}