package com.uade.desarrollo.desarrolloAPP.entity.dto;

import java.time.LocalDate;

public record HistorialMedicoDTO(
    Integer id,
    LocalDate fecha,
    String profesional,
    String motivo,
    String fotoUrl,
    String descripcion,
    Long userId
) {}
