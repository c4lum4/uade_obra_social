package com.uade.desarrollo.desarrolloAPP.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "obra_social")
public class ObraSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombreObraSocial;

    @Column(nullable = false)
    private Long numeroAfiliado;

    @Column(nullable = false)
    private String tipoAfiliado;

    @Column(nullable = false)
    private LocalDate fechaAlta;
}
