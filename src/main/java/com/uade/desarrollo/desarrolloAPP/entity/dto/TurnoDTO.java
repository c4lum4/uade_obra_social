/*package com.uade.desarrollo.desarrolloAPP.entity.dto;

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

    // Nuevo campo: nombre de la obra social asociada al usuario que reservó
    private String obraSocialNombre;
}
*/
package com.uade.desarrollo.desarrolloAPP.entity.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class TurnoDTO {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fecha;

    private Integer profesionalId;
    private String nombre_de_profesional;
    private String especialidad_de_profesional;
    private Long usuarioId;
    private String estado;

    // Nuevo campo: nombre de la obra social asociada al usuario que reservó
    private String obraSocialNombre;
}
