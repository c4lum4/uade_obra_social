package com.uade.desarrollo.desarrolloAPP.entity;

import jakarta.persistence.*;
import lombok.Data;


@Table(name = "resultado_estudio")
@Entity
@Data
public class Resultado_estudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imagenURL;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @OneToOne
    @JoinColumn(name = "id_turno", nullable = false)
    private Turno turno;
}
