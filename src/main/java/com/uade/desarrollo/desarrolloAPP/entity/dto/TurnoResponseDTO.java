/*package com.uade.desarrollo.desarrolloAPP.entity.dto;

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
    */

package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class TurnoResponseDTO {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fecha;

    private String estado;
    private Integer profesionalId;
    private String nombreProfesional;
    private String especialidadProfesional;
    private Long usuarioId;
    private String nombreUsuario;
}
